package com.palgao.menu.modules.Notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.palgao.menu.modules.Notifications.UseCase.GetNotificationsUseCase;
import com.palgao.menu.modules.Notifications.entity.Notification;
import com.palgao.menu.modules.ProgressDialog.SharedLoadingViewModel;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends ViewModel {

    private GetNotificationsUseCase getNotificationsUseCase;
    private MutableLiveData<List<Notification>> notificationsLiveData;
    private MutableLiveData<Notification> realTimeNotificationLiveData;
    private WebSocket webSocket;
    private static final String BASE_URL = "https://pc3ld10h-8080.usw3.devtunnels.ms/api/v1/";
    private final SharedLoadingViewModel sharedLoadingViewModel;


    public NotificationViewModel(SharedLoadingViewModel sharedLoadingViewModel) {
        this.sharedLoadingViewModel = sharedLoadingViewModel;
        NotificationRepository repository = new NotificationRepository();
        getNotificationsUseCase = new GetNotificationsUseCase(repository);
        notificationsLiveData = new MutableLiveData<>();
        realTimeNotificationLiveData = new MutableLiveData<>();
        setupWebSocket();
    }

    public LiveData<List<Notification>> getNotificationsLiveData() {
        return notificationsLiveData;
    }

    public LiveData<Notification> getRealTimeNotificationLiveData() {
        return realTimeNotificationLiveData;
    }

    // Método para cargar las notificaciones desde el backend
    public void loadNotifications(String userId, SharedLoadingViewModel sharedLoadingViewModel) {
        Call<List<Notification>> call = getNotificationsUseCase.execute(userId);
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful()) {
                    notificationsLiveData.postValue(response.body());
                    sharedLoadingViewModel.setLoadingState(false);
                }
                else
                {
                    int a = 0;
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                // Manejar errores
                int a = 0;
            }
        });
    }

    // Método para inicializar WebSocket
    private void setupWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(BASE_URL).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // Aquí recibes notificaciones en tiempo real y las posteas al LiveData
                // Convertir el mensaje JSON a Notification y postearlo
                Notification notification = new Notification(); // Parse JSON a Notification
                realTimeNotificationLiveData.postValue(notification);
            }
        });
    }

    public void sendNotification(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    public void closeWebSocket() {
        if (webSocket != null) {
            webSocket.close(1000, "Closing WebSocket");
        }
    }
}
