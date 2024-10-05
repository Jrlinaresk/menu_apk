// File: io/nxlabs/ui/maps/MapFragment.java
package com.palgao.menu.modules.maps;

import static com.palgao.menu.modules.maps.utils.MapsUtils.distanceBetween;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.doctoror.geocoder.BuildConfig;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import com.palgao.menu.R;

public class MapFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private MapViewModel mapViewModel;
    private LocationRepository locationRepository;
    private MapHandler mapHandler;
    private ProgressBar progressBar;
    private TextView locationTextView;
    private MapView mapView;
    private GeoPoint lastKnownLocation;
    private Button bt_swap_view;
    private WebView web_view;
    private WebViewViewModel webViewViewModel;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        locationRepository = new LocationRepository(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        Configuration.getInstance().setTileDownloadThreads((short) 4);
        Configuration.getInstance().setTileDownloadMaxQueueSize((short) 40);

        initializeViewModel();
        checkAndRequestPermissions();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initializeViews(view);
        inicializarListeners(view);

        if (savedInstanceState == null) {
            mapView.setTileSource(TileSourceFactory.MAPNIK);
            mapView.setMultiTouchControls(true);
        }

        webViewViewModel.getUrl().observe(getViewLifecycleOwner(), url -> {
            if (url != null) {
                web_view.loadUrl(url);
            }
        });

        mapHandler = new MapHandler(requireContext(), mapView, progressBar, locationTextView);

        // ver si es necesario solicitar permisos tambien aqui
        setupWebView();

        return view;
    }

    private void inicializarListeners(View view) {
        bt_swap_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (web_view.getVisibility() == View.VISIBLE)
                {
                    web_view.setVisibility(View.GONE);
                    mapView.setVisibility(View.VISIBLE);
                }
                else {
                    mapView.setVisibility(View.GONE);
                    web_view.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inicializar vistas y configurarlas
        setupObservers();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            // mapView.saveState(outState);
        }
    }


    private void initializeViews(View view) {
        bt_swap_view = view.findViewById(R.id.bt_swap_view);
        web_view = view.findViewById(R.id.web_view);
        progressBar = view.findViewById(R.id.progress_bar);
        locationTextView = view.findViewById(R.id.location_text_view);
        mapView = view.findViewById(R.id.map_view);
    }

    private void initializeViewModel() {
        mapViewModel = new ViewModelProvider(this, new MapViewModelFactory(locationRepository)).get(MapViewModel.class);
        webViewViewModel = new ViewModelProvider(this).get(WebViewViewModel.class);
    }

    private void setupObservers() {
        mapViewModel.getUserLocation().observe(getViewLifecycleOwner(), location -> {
            if (location != null) {
                GeoPoint storeGeoPoint = new GeoPoint(mapViewModel.getStoreLocation().getValue().getLatitude(), mapViewModel.getStoreLocation().getValue().getLongitude());
                GeoPoint newLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                if (lastKnownLocation == null || distanceBetween(
                        lastKnownLocation.getLatitude(),
                        lastKnownLocation.getLongitude(),
                        newLocation.getLatitude(),
                        newLocation.getLongitude()) >= 30) { // 30 es el rango de metros necesario para la proxima actualizacion del mapa
                    lastKnownLocation = newLocation;
                    mapHandler.updateLocationOnMap(location, storeGeoPoint);
                }
            }
        });

        mapViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        mapViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                mapHandler.showSnackbar(errorMessage);
            }
        });
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            mapViewModel.requestUserLocation(requireContext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapViewModel.requestUserLocation(requireContext());
            } else {
                mapHandler.showSnackbar("Location permissions denied. App functionality may be limited.");
            }
        }
    }

    private void setupWebView() {
        if (web_view != null)
        {
            WebView webView = web_view;

            // Configuración general del WebView
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true); // Habilita JavaScript
            webSettings.setMediaPlaybackRequiresUserGesture(false); // Permite la reproducción de medios sin interacción del usuario
            webSettings.setDomStorageEnabled(true); // Habilita almacenamiento DOM
            webSettings.setDatabaseEnabled(true); // Habilita bases de datos
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // Modo de cache
            webSettings.setAllowFileAccess(true); // Permite el acceso a archivos

            // Habilitar el acceso a la cámara y otros recursos
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    getActivity().runOnUiThread(() -> request.grant(request.getResources()));
                }
            });

            // Maneja enlaces dentro del WebView
            webView.setWebViewClient(new WebViewClient());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
        if (mapHandler != null) {
            mapHandler.stopAnimation(); // Detener la animación
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

}
