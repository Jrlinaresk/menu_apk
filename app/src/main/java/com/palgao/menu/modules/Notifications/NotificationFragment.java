package com.palgao.menu.modules.Notifications;

// NotificationFragment.java

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.palgao.menu.PreferencesManager;
import com.palgao.menu.R;
import com.palgao.menu.modules.Notifications.Adapter.NotificationAdapter;
import com.palgao.menu.modules.Notifications.entity.Notification;
import com.palgao.menu.modules.ProgressDialog.SharedLoadingViewModel;
import com.palgao.menu.modules.ui.WaitingDialog;

import java.util.Collections;
import java.util.List;

public class NotificationFragment extends Fragment {

    private NotificationViewModel notificationViewModel;
    private NotificationAdapter adapter;
    private SharedLoadingViewModel sharedLoadingViewModel;
    private WaitingDialog mWaitingDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWaitingDialog = WaitingDialog.show(requireContext(), "Cargando Notificaciones",true);

        sharedLoadingViewModel = new ViewModelProvider(this).get(SharedLoadingViewModel.class);

        sharedLoadingViewModel.getLoadingState().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && !isLoading) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> mWaitingDialog.dismiss(), 1000);
            }
        });

            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(null);
        recyclerView.setAdapter(adapter);

        NotificationViewModelFactory notificationViewModelFactory = new NotificationViewModelFactory(sharedLoadingViewModel);
        notificationViewModel = new ViewModelProvider(this, notificationViewModelFactory).get(NotificationViewModel.class);
        String userID =  PreferencesManager.fromContext(requireContext()).userId();
        notificationViewModel.loadNotifications(userID, sharedLoadingViewModel);

        // Observar los cambios en las notificaciones
        notificationViewModel.getNotificationsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notifications) {
                // Actualizar el adapter cuando las notificaciones cambien
                if (!notifications.isEmpty())
                    Collections.reverse(notifications);
                adapter.updateNotifications(notifications);
            }
        });
    }
}
