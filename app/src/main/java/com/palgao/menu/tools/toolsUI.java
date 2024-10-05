package com.palgao.menu.tools;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.palgao.menu.MasterActivity;
import com.palgao.menu.R;
import com.palgao.menu.modules.products.ui.ProductAdapter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class toolsUI {

    public static void ReplaceFragment(Fragment fragment, int containerId, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commit();
    }

    // Función para cambiar el estado del TextView según la hora actual
    public static void actualizarEstadoLocal(String apertura, String cierre, TextView textView) {
        // Formato de hora HH:mm (24 horas)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Convertimos las horas de apertura y cierre a LocalTime
        LocalTime horaApertura = LocalTime.parse(apertura, formatter);
        LocalTime horaCierre = LocalTime.parse(cierre, formatter);

        // Obtener la hora actual
        LocalTime horaActual = LocalTime.now();

        // Comparar la hora actual con las horas de apertura y cierre
        if (horaActual.isAfter(horaApertura) && horaActual.isBefore(horaCierre)) {
            // Si la hora actual está entre la hora de apertura y la de cierre, el local está abierto
            textView.setText("Abierto");
        } else {
            // Si no, el local está cerrado
            textView.setText("Cerrado");
        }
    }

    public static void setLayoutType(int isHorizontal, RecyclerView recyclerView, Context context, ProductAdapter productAdapter, int item_width, Resources resources) {
        productAdapter.setLayoutType(isHorizontal);
        if (isHorizontal == 1) {
            recyclerView.setLayoutManager(new GridLayoutManager(context, calculateNumberOfColumns(item_width, resources)));
        } else if (isHorizontal == 2) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else if (isHorizontal == 3) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    public static int calculateNumberOfColumns(int item_width, Resources resources) {
        int recyclerViewWidth = resources.getDisplayMetrics().widthPixels;
        int itemWidth = resources.getDimensionPixelSize(R.dimen.item_width);
        return Math.max(1, recyclerViewWidth / itemWidth);
    }

    public static String[] convertHorario(int horaOpen, int horaClose) {
        // Formatear la hora de apertura
        String apertura = String.format("%02d:00", horaOpen);

        // Formatear la hora de cierre
        String cierre = String.format("%02d:00", horaClose);

        return new String[]{apertura, cierre};
    }

    public static void showNotification2(String title, String message, Context context, String NOTIFICATION_CHANNEL_ID, String NOTIFICATION_CHANNEL_NAME) {
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

}
