package com.palgao.menu.modules.Network;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.palgao.menu.R;
import com.palgao.menu.modules.products.data.ProductRepositoryImpl;
import com.palgao.menu.modules.products.ui.ProductsViewModel;
import com.palgao.menu.modules.products.ui.ProductsViewModelFactory;

public class ConnectionFragment extends Fragment {

    private TextView internetStatusTextView;
    private TextView backendStatusTextView;
    private InternetConnectionMonitor internetConnectionMonitor;
    private ProductsViewModel productsViewModel;
    private ConnectionViewModel connectionViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection_status, container, false);

        internetStatusTextView = view.findViewById(R.id.internetStatusTextView);
        backendStatusTextView = view.findViewById(R.id.backendStatusTextView);

        //
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        ProductsViewModelFactory factory = new ProductsViewModelFactory(null, productRepository);
        productsViewModel = new ViewModelProvider(this, factory).get(ProductsViewModel.class);
        connectionViewModel = new ViewModelProvider(this).get(ConnectionViewModel.class);

        //
        // Inicializar los monitores
        internetConnectionMonitor = new InternetConnectionMonitor(getContext(), internetStatusTextView, backendStatusTextView, productsViewModel, connectionViewModel);

        observeConnectionStatus();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Registrar para monitorizar la conexión a Internet
        internetConnectionMonitor.registerNetworkCallback();
        // Conectar WebSocket
    }

    @Override
    public void onPause() {
        super.onPause();
        // Desregistrar el monitor de conexión a Internet
        internetConnectionMonitor.unregisterNetworkCallback();
        // Desconectar el WebSocket
        // webSocketMonitor.disconnectSocket();
    }

    private void observeConnectionStatus() {
        connectionViewModel.getInternetConnectionStatus().observe(getViewLifecycleOwner(), isConnected -> {
            if (isConnected) {
                internetStatusTextView.setVisibility(View.GONE);
            } else {
                internetStatusTextView.setVisibility(View.VISIBLE);
                internetStatusTextView.setText("No Internet Connection");
            }
        });

        connectionViewModel.getBackendConnectionStatus().observe(getViewLifecycleOwner(), isConnected -> {
            if (isConnected) {
                backendStatusTextView.setVisibility(View.GONE);
            } else {
                backendStatusTextView.setVisibility(View.VISIBLE);
                backendStatusTextView.setText("Disconnected from backend");
            }
        });
    }
}
