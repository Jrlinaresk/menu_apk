//package com.palgao.menu.modules.Notifications;
//
//// NotificationWebSocketClient.java
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.os.Build;
//import android.os.IBinder;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//import androidx.lifecycle.MutableLiveData;
//
//import com.palgao.menu.MasterActivity;
//import com.palgao.menu.R;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.WebSocket;
//import okhttp3.WebSocketListener;
//import okio.ByteString;
//
//public class NotificationWebSocketClient extends Service {
//
//    private OkHttpClient client;
//    private WebSocket webSocket;
//    private MutableLiveData<String> notificationLiveData;
//    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel";
//    private static final String BASE_URL = "https://pc3ld10h-8080.usw3.devtunnels.ms/";
//
//
//    public NotificationWebSocketClient() {
//        client = new OkHttpClient();
//        notificationLiveData = new MutableLiveData<>();
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        createNotificationChannel();
//
//        // Crear una notificación persistente
//        Intent notificationIntent = new Intent(this, MasterActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                .setContentTitle("WebSocket Service")
//                .setContentText("Listening to WebSocket")
//                .setSmallIcon(R.drawable.ic_notificaciones_empty)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        startForeground(1, notification);
//
//        // Conectar al WebSocket
//        connectWebSocket(BASE_URL);
//    }
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;  // Para que el servicio se reinicie si el sistema lo detiene
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (webSocket != null) {
//            webSocket.close(1000, "Service Destroyed");
//        }
//    }
//
//    private void showNotification(String title, String message) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_notificaciones_empty)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(2, builder.build());
//    }
//
//    // Crear el canal de notificación para Android Oreo y superior
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel serviceChannel = new NotificationChannel(
//                    NOTIFICATION_CHANNEL_ID,
//                    "WebSocket Service Channel",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(serviceChannel);
//        }
//    }
//
//    // Conectar a WebSocket
//    public void connectWebSocket(String url) {
//        Request request = new Request.Builder().url(url).build();
//        webSocket = client.newWebSocket(request, new WebSocketListener() {
//            @Override
//            public void onMessage(WebSocket webSocket, String text) {
//                // Actualizar LiveData con las notificaciones recibidas
//                notificationLiveData.postValue(text);
//            }
//
//            @Override
//            public void onMessage(WebSocket webSocket, ByteString bytes) {
//                notificationLiveData.postValue(bytes.toString());
//            }
//        });
//    }
//
//    // Enviar datos al WebSocket
//    public void sendMessage(String message) {
//        webSocket.send(message);
//    }
//
//    // Cerrar conexión
//    public void closeWebSocket() {
//        webSocket.close(1000, "Closing WebSocket");
//    }
//
//    public MutableLiveData<String> getNotificationLiveData() {
//        return notificationLiveData;
//    }
//}
