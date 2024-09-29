package com.palgao.menu.tools;

import android.content.Context;
import android.content.res.Resources;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
}
