package com.palgao.menu.modules.Notifications;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.palgao.menu.modules.ProgressDialog.SharedLoadingViewModel;

public class NotificationViewModelFactory implements ViewModelProvider.Factory {
    private final SharedLoadingViewModel sharedLoadingViewModel;

    public NotificationViewModelFactory(SharedLoadingViewModel sharedLoadingViewModel) {
        this.sharedLoadingViewModel = sharedLoadingViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NotificationViewModel.class)) {
            return (T) new NotificationViewModel(sharedLoadingViewModel);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

