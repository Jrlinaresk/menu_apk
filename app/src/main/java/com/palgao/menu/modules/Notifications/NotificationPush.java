package com.palgao.menu.modules.Notifications;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.palgao.menu.MasterActivity;
import com.palgao.menu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class NotificationPush extends Service {

    private static final String BASE_URL = "https://pc3ld10h-8080.usw3.devtunnels.ms/";
    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel";
    private static final String NOTIFICATION_CHANNEL_NAME = "Notifications";
    private Socket socket;
    public NotificationPush() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupSocketIO();
        createNotificationChannel();  // Crear el canal de notificación
        startForegroundService();     // Iniciar el servicio en primer plano
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundService();     // Asegurarse de que el servicio esté en primer plano
        return START_STICKY;          // El servicio se reiniciará si es detenido
    }

    // Configura el WebSocket
    private void setupSocketIO() {
        try {
            IO.Options options = new IO.Options();
            options.forceNew = true;  // Otras opciones si es necesario
            socket = IO.socket(BASE_URL, options);
            socket.on(Socket.EVENT_CONNECT, args -> {
                // Acciones al conectarse
            }).on("notificationCreated", args -> {
                try {
                    if (args[0] instanceof JSONArray) {
                        JSONArray notificationsArray = (JSONArray) args[0];
                        for (int i = 0; i < notificationsArray.length(); i++) {
                            JSONObject notificationObject = notificationsArray.getJSONObject(i);
                            String title = notificationObject.getString("title");
                            String content = notificationObject.getString("content");
                            showNotification(title, content);
                        }
                    } else if (args[0] instanceof JSONObject) {
                        JSONObject notificationObject = (JSONObject) args[0];
                        String title = notificationObject.getString("title");
                        String content = notificationObject.getString("content");
                        showNotification(title, content);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).on(Socket.EVENT_DISCONNECT, args -> {
                // Acciones al desconectarse
            });

            socket.connect();

        } catch (URISyntaxException e) {
            Log.e("SocketIO", "Error en URI: " + e.getMessage());
        }
    }

    // Crear el canal de notificación (necesario para Android 8.0+)
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH // Cambia a IMPORTANCE_HIGH
        );
        channel.setDescription("Canal para notificaciones de servicio");
        channel.enableVibration(true); // Habilitar vibración
        channel.setVibrationPattern(new long[]{0, 1000}); // Patrón de vibración
        channel.enableLights(true); // Habilitar luces
        channel.setLightColor(Color.RED); // Color de la luz
        channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()); // Configurar sonido predeterminado para la notificación

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }


    // Inicia el servicio en primer plano con la notificación
    private void startForegroundService() {
        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Servicio de notificaciones")
                .setContentText("El servicio está ejecutándose en segundo plano")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(1, notification);  // Iniciar el servicio con la notificación
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (socket != null && socket.connected()) {
            socket.disconnect();
            socket.close();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Muestra la notificación en el dispositivo
    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, MasterActivity.class);  // Asegúrate de usar 'this' para el contexto
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);

        // Patrón de vibración
        long[] vibrationPattern = {0, 1000};  // Espera 0 ms y vibra 1000 ms

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(vibrationPattern)  // Patrón de vibración
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)  // Permite el clic
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Mostrar la notificación
        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }

}
