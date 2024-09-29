package com.palgao.menu.tools;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import androidx.loader.content.CursorLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Tools {
    public static String cleanNullSubstrings(String text) {
        return text.replaceAll("\\bnull\\b", "").replaceAll(",\\s*,", ",").trim();
    }

    // Método para calcular el tiempo relativo
    public static String getRelativeTime(String createdAt) {
        // Formato de fecha que coincide con el formato de tu "createdAt"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            // Parsear la fecha de creación
            Date createdDate = dateFormat.parse(createdAt);

            // Obtener la fecha actual
            Date currentDate = new Date();

            // Calcular la diferencia en milisegundos
            long duration = currentDate.getTime() - createdDate.getTime();

            // Convertir la diferencia a segundos, minutos, horas, días
            long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            long hours = TimeUnit.MILLISECONDS.toHours(duration);
            long days = TimeUnit.MILLISECONDS.toDays(duration);

            // Mostrar el tiempo relativo
            if (seconds < 1)
                return "1s";
            else if (seconds < 60) {
                return seconds + "s"; // Segundos
            } else if (minutes < 60) {
                return minutes + "m"; // Minutos
            } else if (hours < 24) {
                return hours + "h"; // Horas
            } else if (days < 7) {
                return days + "d"; // Días
            } else if (days < 30) {
                return (days / 7) + "w"; // Semanas
            } else if (days < 365) {
                return (days / 30) + "M"; // Meses
            } else {
                return (days / 365) + "y"; // Años
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ""; // En caso de error
    }

    public static long getFileSize(Uri uri, Context context) {
        String[] projection = {MediaStore.Images.Media.SIZE};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int sizeColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            cursor.moveToFirst();
            long size = cursor.getLong(sizeColumnIndex);
            cursor.close();
            return size;
        }
        return 0;
    }

    public static String getRealPathFromURI(Uri contentUri, Context context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    private void openGallery(int PICK_IMAGE_REQUEST, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(activity, intent, PICK_IMAGE_REQUEST, null);
    }

    // Método para obtener el nombre del archivo desde el Uri
    public static String getFileName(Uri uri, Context context) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    // Obtiene el índice del nombre del archivo
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    result = cursor.getString(nameIndex);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
