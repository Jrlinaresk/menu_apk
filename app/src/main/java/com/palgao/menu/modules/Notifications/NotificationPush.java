package com.palgao.menu.modules.Notifications;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.palgao.menu.MasterActivity;
import com.palgao.menu.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class NotificationPush {
    private static final String BASE_URL = "https://pc3ld10h-8080.usw3.devtunnels.ms/";
    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel";
    private static final String NOTIFICATION_CHANNEL_NAME = "Notifications";
    private Context context;
    private Socket socket;
    public NotificationPush(Context context) {
        this.context = context;
        setupSocketIO();
        createNotificationChannel();
    }

    // Configura el WebSocket
    private void setupSocketIO() {
        try {
            IO.Options options = new IO.Options();
            options.forceNew = true;  // Otras opciones si es necesario
            socket = IO.socket(BASE_URL, options);
            socket.on(Socket.EVENT_CONNECT, args -> {
                int a = 0;
            }).on("notificationCreated", args -> {
                try {
                    // Verifica si args[0] es un JSONArray
                    if (args[0] instanceof JSONArray) {
                        JSONArray notificationsArray = (JSONArray) args[0];
                        for (int i = 0; i < notificationsArray.length(); i++) {
                            JSONObject notificationObject = notificationsArray.getJSONObject(i);
                            String title = notificationObject.getString("title");
                            String content = notificationObject.getString("content");
                            showNotification(title, content);
                        }
                    } else if (args[0] instanceof JSONObject) {
                        // Si solo recibes un objeto de notificación
                        JSONObject notificationObject = (JSONObject) args[0];
                        String title = notificationObject.getString("title");
                        String content = notificationObject.getString("content");
                        showNotification(title, content);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).on("notificationUpdated", args -> {
                try {
                    // Verifica si args[0] es un JSONArray
                    if (args[0] instanceof JSONArray) {
                        JSONArray notificationsArray = (JSONArray) args[0];
                        for (int i = 0; i < notificationsArray.length(); i++) {
                            JSONObject notificationObject = notificationsArray.getJSONObject(i);
                            String title = notificationObject.getString("title");
                            String content = notificationObject.getString("content");
                            showNotification(title, content);
                        }
                    } else if (args[0] instanceof JSONObject) {
                        // Si solo recibes un objeto de notificación
                        JSONObject notificationObject = (JSONObject) args[0];
                        String title = notificationObject.getString("title");
                        String content = notificationObject.getString("content");
                        showNotification(title, content);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).on(Socket.EVENT_DISCONNECT, args -> {
                int a = 0;
            }).on("notificationsRetrieved", args -> {
                JSONObject notifications = (JSONObject) args[0];
                try {
                    showNotification(notifications.getString("title"), notifications.getString("content"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // Manejar la lista de notificaciones recibidas
            }).on("notificationRead", args -> {
                JSONObject notification = (JSONObject) args[0];
                // Manejar la notificación marcada como leída
            });

            socket.connect();

        } catch (URISyntaxException e) {
            Log.e("SocketIO", "Error en URI: " + e.getMessage());
        }
    }
    // Muestra la notificación en el dispositivo
    private void showNotification(String title, String message) {
        Intent intent = new Intent(context, MasterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Sonido personalizado
        // Uri customSoundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.tu_sonido_personalizado);

        // Patrón de vibración
        long[] vibrationPattern = {0, 1000}; // Espera 0 ms y vibra 500 ms

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                //.setSound(customSoundUri) // Sonido personalizado
                .setVibrate(vibrationPattern) // Patrón de vibración
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setGroup("your_group_key");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            if (channel == null) {
                channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Descripción del canal de notificaciones");
                // channel.setSound(customSoundUri, null); // Asigna el sonido al canal
                channel.enableVibration(true); // Habilita la vibración en el canal
                channel.setVibrationPattern(vibrationPattern); // Establece el patrón de vibración
                notificationManager.createNotificationChannel(channel);
            }
        }

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }


    // Crear el canal de notificación (necesario para Android 8.0+)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notification Channel Description");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //

    // Emitir evento 'createNotification'
    public void createNotification(JSONObject notificationData) {
        if (socket != null && socket.connected()) {
            socket.emit("createNotification", notificationData);
        } else {
            Log.e("SocketIO", "Socket no conectado");
        }
    }

    // Emitir evento 'getNotifications'
    public void getNotifications(String userId) {
        if (socket != null && socket.connected()) {
            socket.emit("getNotifications", userId);
        } else {
            Log.e("SocketIO", "Socket no conectado");
        }
    }

    // Emitir evento 'markNotificationRead'
    public void markNotificationRead(String notificationId) {
        if (socket != null && socket.connected()) {
            socket.emit("markNotificationRead", notificationId);
        } else {
            Log.e("SocketIO", "Socket no conectado");
        }
    }

    private void showSnackbar(String message, boolean isUndoAction) {
        // Asegúrate de que `context` sea una actividad y no un contexto de aplicación
        if (context instanceof Activity) {
            View view = ((Activity) context).findViewById(android.R.id.content);

            // Inflar el diseño personalizado
            View customView = LayoutInflater.from(context).inflate(R.layout.custom_snackbar, null);

            TextView snackbarText = customView.findViewById(R.id.snackbar_text);
            Button snackbarAction = customView.findViewById(R.id.snackbar_action);
            snackbarText.setText(message);

            // Configurar la acción del botón
            snackbarAction.setOnClickListener(v -> {
                context.startActivity(new Intent(context, MasterActivity.class));
            });

            Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
            Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
            snackbarLayout.setPadding(0, 8, 0, 0); // Quitar padding

            // Agregar el diseño personalizado al Snackbar
            snackbarLayout.addView(customView, 0);
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onShown(Snackbar sb) {
                    super.onShown(sb);
                    // Aquí puedes realizar alguna acción cuando el Snackbar se muestra
                    Log.d("Snackbar", "Snackbar mostrado");
                }

                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    // Aquí puedes realizar alguna acción cuando el Snackbar se oculta
                    Log.d("Snackbar", "Snackbar ocultado");
                }
            });

            // Mostrar el Snackbar
            snackbar.show();
        }
    }



    //

    // Cierra el WebSocket cuando ya no se necesite
    public void closeSocket() {
        if (socket != null) {
            socket.disconnect();
            socket.close();
        }
    }
}
