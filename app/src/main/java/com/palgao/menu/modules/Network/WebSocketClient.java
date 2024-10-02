//package com.palgao.menu.modules.Network;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.WebSocket;
//import okhttp3.WebSocketListener;
//import okio.ByteString;
//import java.util.concurrent.TimeUnit;
//
//public class WebSocketClient {
//    private OkHttpClient client;
//    private WebSocket webSocket;
//    private final String serverUrl = "https://pc3ld10h-8080.usw3.devtunnels.ms/";
//    private WebSocketListener listener;
//
//    public WebSocketClient(WebSocketListener listener) {
//        this.listener = listener;
//        client = new OkHttpClient.Builder()
//                .pingInterval(30, TimeUnit.SECONDS) // Ping cada 30s para mantener viva la conexión
//                .build();
//    }
//
//    public void connect() {
//        Request request = new Request.Builder().url(serverUrl).build();
//        webSocket = client.newWebSocket(request, listener);
//    }
//
//    public void disconnect() {
//        if (webSocket != null) {
//            webSocket.close(1000, "Cerrando conexión");
//        }
//    }
//
//    public void reconnect() {
//        disconnect();
//        connect();
//    }
//
//    public WebSocket getWebSocket() {
//        return webSocket;
//    }
//}
//
