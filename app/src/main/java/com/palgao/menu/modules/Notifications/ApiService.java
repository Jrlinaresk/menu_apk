package com.palgao.menu.modules.Notifications;

import com.palgao.menu.modules.Notifications.entity.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("notifications/user/{userId}")
    Call<List<Notification>> getNotificationsByUser(@Path("userId") String userId);

    @POST("notifications")
    Call<Void> createNotification(@Body Notification notification);

    @PATCH("notifications/{id}/mark-read")
    Call<Void> markNotificationAsRead(@Path("id") String notificationId);
}
