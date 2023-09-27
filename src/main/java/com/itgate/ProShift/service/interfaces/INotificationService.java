package com.itgate.ProShift.service.interfaces;

import com.itgate.ProShift.entity.Notification;
import java.util.List;

public interface INotificationService {
    List<Notification> getAllNotifications();
    List<Notification> getNotificationsByUserId(Long userId);
    Notification createNotification(Notification notification);
    Notification updateNotification(Notification notification);
    void deleteNotification(Long notificationId);
}
