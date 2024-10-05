// File: io/nxlabs/ui/maps/MapHandler.java
package com.palgao.menu.modules.maps;

import static com.palgao.menu.modules.maps.utils.MapsUtils.decodePolyline;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import com.palgao.menu.R;
import com.palgao.menu.modules.maps.OSRM.OSRMResponse;
import com.palgao.menu.modules.maps.OSRM.OSRMService;
import com.palgao.menu.modules.maps.OSRM.RetrofitClient;
import retrofit2.Call;

public class MapHandler {
    private Context context;
    private MapView mapView;
    private IMapController mapController;
    private ProgressBar progressBar;
    private TextView locationTextView;
    private List<List<Double>> coordinates;

    private ValueAnimator routeAnimator; // Añade esta variable a la clase MapHandler


    public MapHandler(Context context, MapView mapView, ProgressBar progressBar, TextView locationTextView) {
        this.context = context;
        this.mapView = mapView;
        this.progressBar = progressBar;
        this.locationTextView = locationTextView;
        initializeMapView();
    }

    private void initializeMapView() {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE));
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        mapController = mapView.getController();
        mapController.setZoom(18.0);

        mapView.getTileProvider().setTileRequestCompleteHandler(new SimpleInvalidationHandler(mapView));
    }

    public void updateLocationOnMap(Location location, GeoPoint storeGeoPoint) {
        locationTextView.setText("User Location: " + location.getLatitude() + ", " + location.getLongitude());
        GeoPoint userGeoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        GeoPoint userGeoPointFake = new GeoPoint(19.36899577932705, -99.178466410177);

        // Calcular el punto medio entre userGeoPointFake y storeGeoPoint
        double midLat = (userGeoPointFake.getLatitude() + storeGeoPoint.getLatitude()) / 2;
        double midLon = (userGeoPointFake.getLongitude() + storeGeoPoint.getLongitude()) / 2;
        GeoPoint midpoint = new GeoPoint(midLat, midLon);

        // Establecer el centro del mapa en el punto medio
        mapController.setCenter(midpoint);

        // Ajustar el zoom para asegurar que ambos puntos sean visibles
        BoundingBox boundingBox = new BoundingBox(
                Math.max(userGeoPointFake.getLatitude(), storeGeoPoint.getLatitude()),
                Math.max(userGeoPointFake.getLongitude(), storeGeoPoint.getLongitude()),
                Math.min(userGeoPointFake.getLatitude(), storeGeoPoint.getLatitude()),
                Math.min(userGeoPointFake.getLongitude(), storeGeoPoint.getLongitude())
        );

        // Crear y agregar los marcadores
        Marker userMarker = new Marker(mapView);
        userMarker.setPosition(userGeoPointFake);
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        userMarker.setTitle("You are here");
        // Cargar el recurso del icono
        Drawable userIcon = ContextCompat.getDrawable(context, R.drawable.ic_location);
        userMarker.setIcon(userIcon);

        Marker userMarkerStore = new Marker(mapView);
        userMarkerStore.setPosition(storeGeoPoint);
        userMarkerStore.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        userMarkerStore.setTitle("Attempo.com");
        Drawable storeIcon = ContextCompat.getDrawable(context, R.drawable.ic_star_filled);
        userMarkerStore.setIcon(storeIcon);
        userMarkerStore.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                userMarkerStore.showInfoWindow();
                Toast.makeText(context, "Bienvenido!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        mapView.getOverlays().clear();
        mapView.getOverlays().add(userMarker);
        mapView.getOverlays().add(userMarkerStore);

        fetchAndDrawRoute(userGeoPointFake, storeGeoPoint);

        mapView.invalidate();
    }

    public void fetchAndDrawRoute(GeoPoint startPoint, GeoPoint endPoint) {
        OSRMService service = RetrofitClient.getClient().create(OSRMService.class);

        Call<OSRMResponse> call = service.getRoute(
                startPoint.getLongitude(),
                startPoint.getLatitude(),
                endPoint.getLongitude(),
                endPoint.getLatitude(),
                "false",
                true
        );

        call.enqueue(new retrofit2.Callback<OSRMResponse>() {
            @Override
            public void onResponse(Call<OSRMResponse> call, retrofit2.Response<OSRMResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("OSRMResponse: ", jsonResponse);

                    List<List<Double>> allCoordinates = new ArrayList<>();
                    for (OSRMResponse.Route route : response.body().routes) {
                        for (OSRMResponse.Leg leg : route.legs) {
                            for (OSRMResponse.Step step : leg.steps) {
                                List<List<Double>> stepCoordinates = decodePolyline(step.geometry);
                                allCoordinates.addAll(stepCoordinates);
                            }
                        }
                    }

                    if (allCoordinates != null && allCoordinates.size() >= 2) {
                        drawRouteFrom_Strat_To_end(allCoordinates);
                    } else {
                        showSnackbar("Not enough coordinates to draw the route.");
                    }
                } else {
                    showSnackbar("Failed to get route.");
                }
            }

            @Override
            public void onFailure(Call<OSRMResponse> call, Throwable t) {
                showSnackbar("Error: " + t.getMessage());
            }
        });
    }


    private void drawRouteFrom_A_To_B(GeoPoint startPoint, GeoPoint endPoint) {
        Polyline line = new Polyline();
        line.setTitle("Route");
        line.setSubDescription(Polyline.class.getCanonicalName());
        line.setWidth(10f);
        line.setColor(context.getResources().getColor(R.color.rute_line, null));

        ArrayList<GeoPoint> points = new ArrayList<>();
        points.add(startPoint);
        points.add(endPoint);
        line.setPoints(points);

        mapView.getOverlays().add(line);
    }

    private void drawRouteFrom_Strat_To_end(List<List<Double>> coordinates) {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(context.getResources().getColor(R.color.rute_line, null)); // Verde

        for (int i = 0; i < coordinates.size() - 1; i++) {
            List<Double> start = coordinates.get(i);
            List<Double> end = coordinates.get(i + 1);

            Polyline line = new Polyline();
            line.setTitle("Route Segment " + (i + 1));
            line.setSubDescription(Polyline.class.getCanonicalName());
            line.setWidth(10f);
            line.setColor(colors.get(i % colors.size()));

            ArrayList<GeoPoint> points = new ArrayList<>();
            points.add(new GeoPoint(start.get(0), start.get(1)));
            points.add(new GeoPoint(end.get(0), end.get(1)));
            line.setPoints(points);

            mapView.getOverlays().add(line);
        }

        // Crear una ruta animada
        animateRoute(coordinates);
    }

    private void animateRoute(List<List<Double>> coordinates) {
        final Polyline animatedLine = new Polyline();
        animatedLine.setWidth(10f);
        animatedLine.setColor(context.getResources().getColor(R.color.route_anim, null)); // Color de la animación
        mapView.getOverlays().add(animatedLine);

        // Calcular la distancia total de la ruta
        double totalDistance = 0;
        for (int i = 0; i < coordinates.size() - 1; i++) {
            GeoPoint start = new GeoPoint(coordinates.get(i).get(0), coordinates.get(i).get(1));
            GeoPoint end = new GeoPoint(coordinates.get(i + 1).get(0), coordinates.get(i + 1).get(1));
            totalDistance += start.distanceToAsDouble(end);
        }

        // Calcular la duración total de la animación basada en la distancia total
        final long totalDuration = 2500; // Duración total de la animación en milisegundos

        routeAnimator = ValueAnimator.ofFloat(0, (float) totalDistance);
        routeAnimator.setDuration(totalDuration);
        routeAnimator.setInterpolator(new LinearInterpolator()); // Hacer la animación lineal
        routeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                double accumulatedDistance = 0;
                ArrayList<GeoPoint> points = new ArrayList<>();

                for (int i = 0; i < coordinates.size() - 1; i++) {
                    GeoPoint start = new GeoPoint(coordinates.get(i).get(0), coordinates.get(i).get(1));
                    GeoPoint end = new GeoPoint(coordinates.get(i + 1).get(0), coordinates.get(i + 1).get(1));
                    double segmentDistance = start.distanceToAsDouble(end);

                    if (accumulatedDistance + segmentDistance >= animatedValue) {
                        double fraction = (animatedValue - accumulatedDistance) / segmentDistance;
                        double lat = start.getLatitude() + fraction * (end.getLatitude() - start.getLatitude());
                        double lon = start.getLongitude() + fraction * (end.getLongitude() - start.getLongitude());
                        points.add(new GeoPoint(lat, lon));
                        break;
                    } else {
                        accumulatedDistance += segmentDistance;
                        points.add(end);
                    }
                }

                animatedLine.setPoints(points);
                mapView.invalidate();
            }
        });

        routeAnimator.setRepeatCount(ValueAnimator.INFINITE); // Repetir indefinidamente
        routeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {}

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {
                animatedLine.setPoints(new ArrayList<>()); // Limpiar puntos
            }
        });

        routeAnimator.start();
    }

    // Añade un método para detener la animación
    public void stopAnimation() {
        if (routeAnimator != null) {
            routeAnimator.cancel();
            routeAnimator = null;
        }
    }


    public void showSnackbar(String message) {
        Snackbar.make(mapView, message, Snackbar.LENGTH_LONG).show();
    }
}
