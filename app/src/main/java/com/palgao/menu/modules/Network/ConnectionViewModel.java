// ConnectionViewModel.java
package com.palgao.menu.modules.Network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConnectionViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isInternetConnected = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isBackendConnected = new MutableLiveData<>();

    // Métodos para actualizar el estado de la conexión
    public void setInternetConnected(boolean connected) {
        isInternetConnected.postValue(connected);
    }

    public void setBackendConnected(boolean connected) {
        isBackendConnected.postValue(connected);
    }

    // Métodos para obtener el estado de la conexión
    public LiveData<Boolean> getInternetConnectionStatus() {
        return isInternetConnected;
    }

    public LiveData<Boolean> getBackendConnectionStatus() {
        return isBackendConnected;
    }
}
