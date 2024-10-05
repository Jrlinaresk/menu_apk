package com.palgao.menu.modules.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.palgao.menu.modules.products.ui.ProductsViewModel;

public class InternetConnectionMonitor {

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private TextView internetStatusTextView, backendStatusTextView;
    private WebSocketMonitor webSocketMonitor;
    private ProductsViewModel productsViewModel;
    private ConnectionViewModel connectionViewModel;


    public InternetConnectionMonitor(Context context, TextView internetStatusTextView, TextView backendStatusTextView, ProductsViewModel productsViewModel, ConnectionViewModel connectionViewModel) {
        this.productsViewModel = productsViewModel;
        this.backendStatusTextView = backendStatusTextView;
        this.internetStatusTextView = internetStatusTextView;
        this.connectionViewModel = connectionViewModel;

        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                updateInternetStatus("Connected", 1);
                connectionViewModel.setInternetConnected(true); // Actualizar estado de conexión a Internet

            }

            @Override
            public void onLost(Network network) {
                updateInternetStatus("No Internet Connection", 0);
                connectionViewModel.setInternetConnected(false); // Actualizar estado de desconexión
            }
        };
    }

    public void registerNetworkCallback() {
        NetworkRequest networkRequest = new NetworkRequest.Builder().build();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    public void unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    private void updateInternetStatus(String mensaje, int status) {
        switch (status)
        {
            case 0:
            {
                new Handler(Looper.getMainLooper()).post(() -> {
                    internetStatusTextView.setText(mensaje);
                    internetStatusTextView.setVisibility(View.VISIBLE);
                    backendStatusTextView.setVisibility(View.GONE);
                });

                break;
            }
            case 1:
            {
                new Handler(Looper.getMainLooper()).post(() -> {
                    webSocketMonitor = new WebSocketMonitor(backendStatusTextView, productsViewModel, connectionViewModel);
                    internetStatusTextView.setText(mensaje);
                    internetStatusTextView.setVisibility(View.GONE); // o View.GONE según sea necesario
                });

                break;
            }
        }
    }
}
