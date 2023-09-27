package com.itgate.ProShift.repository;

import com.itgate.ProShift.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findNotificationsByUser_id(Long userid);
}
