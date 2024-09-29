package com.palgao.menu.modules.Notifications;// AuthRepository.java
import com.palgao.menu.modules.Notifications.entity.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationRepository {

    private final ApiService apiService;
    private static final String BASE_URL = "https://pc3ld10h-8080.usw3.devtunnels.ms/api/v1/";

    public NotificationRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public Call<List<Notification>> getNotificationsByUser(String userId) {
        return apiService.getNotificationsByUser(userId);
    }

    public Call<Void> createNotification(Notification notification) {
        return apiService.createNotification(notification);
    }

    public Call<Void> markNotificationAsRead(String notificationId) {
        return apiService.markNotificationAsRead(notificationId);
    }
}
