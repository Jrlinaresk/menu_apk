package com.palgao.menu.modules.Notifications.UseCase;

// GetNotificationsUseCase.java

import com.palgao.menu.modules.Notifications.NotificationRepository;
import com.palgao.menu.modules.Notifications.entity.Notification;

import java.util.List;

import retrofit2.Call;

public class GetNotificationsUseCase {

    private NotificationRepository notificationRepository;

    public GetNotificationsUseCase(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Call<List<Notification>> execute(String userId) {
        return notificationRepository.getNotificationsByUser(userId);
    }
}
