package com.palgao.menu.modules.ProgressDialog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedLoadingViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<Boolean> getLoadingState() {
        return isLoading;
    }

    public void setLoadingState(boolean loading) {
        isLoading.setValue(loading);
    }
}
