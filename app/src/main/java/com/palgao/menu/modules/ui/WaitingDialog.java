package com.palgao.menu.modules.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.palgao.menu.R;

public class WaitingDialog extends Dialog {

    public WaitingDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Evitar que el diálogo se cierre al tocar fuera de él o al presionar el botón de retroceso
        setCancelable(false);
    }

    public static WaitingDialog show(Context context,
                                     boolean showLoading) {
        return show(context, "Por favor espere!", showLoading);
    }

    public static WaitingDialog show(Context context, CharSequence message,
                                     boolean showLoading) {

        WaitingDialog dialog = new WaitingDialog(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_waiting,
                null, false);

        TextView messageView = view.findViewById(R.id.progressMessage);
        ProgressBar progress = view.findViewById(R.id.progress);
        progress.getIndeterminateDrawable().setColorFilter(ContextCompat
                .getColor(context, R.color.c1), PorterDuff.Mode.SRC_ATOP);

        if (showLoading) progress.setVisibility(View.VISIBLE);
        else progress.setVisibility(View.INVISIBLE);

        if (message != null && message.length() > 0)
            messageView.setText(message);

        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.DialogFadeInAnimation);
        dialog.getWindow().getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.setCancelable(false);
        dialog.show();

        return dialog;
    }
}