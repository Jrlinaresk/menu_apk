// NotificationAdapter.java
package com.palgao.menu.modules.Notifications.Adapter;

import static com.palgao.menu.tools.Tools.getRelativeTime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.palgao.menu.R;
import com.palgao.menu.modules.Notifications.entity.Notification;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> notificationList;

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        if (notification.getStatus().equals("pending"))
            holder.cl_background.setBackgroundResource(R.color.notificationNew);
        else
            holder.cl_background.setBackgroundResource(R.color.c0);

        holder.cl_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.getStatus().equals("pending"))
                    holder.cl_background.setBackgroundResource(R.color.c0);
                // cambiar el estado a leida ahora... pendiente
            }
        });

        // Título
        holder.textViewTitle.setText(notification.getTitle());

        // Contenido
        holder.textViewContent.setText(notification.getContent());

        // Icono cargado con Picasso
        Picasso.get()
                .load(notification.getIcon())  // URL del icono
                .placeholder(R.drawable.ic_notificaciones_empty)  // Imagen de placeholder
                .error(R.drawable.ic_closet)  // Imagen en caso de error
                .into(holder.imageViewIcon);

        // Tipo de notificación
        // nada por el momento

        // Grupo de notificación
        // agrupar

        // Estado de la notificación
        // cambiar el background cuando esta leida

        // Fecha de creación
        // Formatear la fecha
        String relativeTime = getRelativeTime(notification.getCreatedAt());
        holder.textViewDate.setText(relativeTime);
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle, textViewContent, textViewDate;
        public ImageView imageViewIcon;
        public ConstraintLayout cl_background;

        public ViewHolder(View view) {
            super(view);
            cl_background = view.findViewById(R.id.cl_background);
            textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewContent = view.findViewById(R.id.textViewContent);
            textViewDate = view.findViewById(R.id.textViewDate);
            imageViewIcon = view.findViewById(R.id.imageViewIcon);
        }
    }

    // Método para actualizar la lista
    public void updateNotifications(List<Notification> notifications) {
        this.notificationList = notifications;
        notifyDataSetChanged();
    }
}
