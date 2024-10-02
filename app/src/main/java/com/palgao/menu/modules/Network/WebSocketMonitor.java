package com.palgao.menu.modules.Network;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.palgao.menu.modules.products.data.Product;
import com.palgao.menu.modules.products.ui.ProductsViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class WebSocketMonitor {

    private Socket socket;
    private TextView backendStatusTextView;
    private String BASE_URL = "https://pc3ld10h-8080.usw3.devtunnels.ms"; // Asegúrate de usar "https" o "wss" si es seguro
    private ProductsViewModel productsViewModel;

    public WebSocketMonitor(TextView backendStatusTextView, ProductsViewModel productsViewModel) {
        this.productsViewModel = productsViewModel;
        this.backendStatusTextView = backendStatusTextView;
        setupSocketIO();
    }

    // Configurar el Socket.IO con las opciones necesarias
    private void setupSocketIO() {
        try {
            IO.Options options = new IO.Options();
            options.timeout = 2000; // Tiempo de espera para la conexión (5 segundos)
            options.reconnectionDelay = 1000; // Retraso de reconexión (1 segundo)
            options.forceNew = true;  // Siempre forzar una nueva conexión
            // Puedes agregar más opciones según las necesidades del servidor, como timeout, etc.

            // Construir la URL con el token si es necesario
            socket = IO.socket(BASE_URL, options);

            // Manejar los eventos del socket
            socket.on(Socket.EVENT_CONNECT, args -> {
                updateBackendStatus("Connected to backend", 1);
            }).on(Socket.EVENT_DISCONNECT, args -> {
                updateBackendStatus("Disconnected from backend", 0);
            }).on(Socket.EVENT_CONNECT_ERROR, args -> {
                updateBackendStatus("Failed to connect to backend", -1);
                Log.e("SocketIO", "Error de conexión: " + args[0]);
                // Intentar reconectar en 30 segundos
                new Handler(Looper.getMainLooper()).postDelayed(this::reconnect, 1500);
            });

            // Conectar el socket
            socket.connect();
        } catch (URISyntaxException e) {
            Log.e("SocketIO", "Error en URI: " + e.getMessage());
        }
    }

    // Método para actualizar el estado del backend en la UI
    private void updateBackendStatus(String mensaje, int status) {
        switch (status)
        {
            case 0:
            {
                new Handler(Looper.getMainLooper()).post(() -> {
                    backendStatusTextView.setText(mensaje);
                    backendStatusTextView.setVisibility(View.VISIBLE); // o View.GONE según sea necesario
                });

                break;
            }
            case 1:
            {
                new Handler(Looper.getMainLooper()).post(() -> {
                    backendStatusTextView.setText(mensaje);
                    backendStatusTextView.setVisibility(View.GONE); // o View.GONE según sea necesario
                });

                break;
            }
            case -1:
            {
                new Handler(Looper.getMainLooper()).post(() -> {
                    backendStatusTextView.setText(mensaje);
                    backendStatusTextView.setVisibility(View.VISIBLE); // o View.GONE según sea necesario
                });

                int a = 0;
                break;
            }
        }
    }

    // Método para desconectar el socket
    public void disconnectSocket() {
        if (socket != null && socket.connected()) {
            socket.disconnect();
            socket.close();
        }
    }

    // Método para reconectar si es necesario
    private void reconnect() {
        if (socket != null && !socket.connected()) {
            socket.connect();
        }
    }

    // Si es necesario, puedes emitir eventos con este método
    public void sendEvent(String event, Object... args) {
        if (socket != null && socket.connected()) {
            socket.emit(event, args);
        } else {
            Log.e("SocketIO", "Socket no conectado, no se puede enviar evento");
        }
    }
    //

    private void initializeSocketListeners() {
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        // Escuchar el evento 'productsUpdated' desde el servidor
        socket
                .on("", args -> {

                });
    }

}