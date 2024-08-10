package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.notification.Notification;
import com.ahuynh.muzi_music_api.model.entity.notification.NotificationStatus;
import com.ahuynh.muzi_music_api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUser(User user);
    List<Notification> findAllByStatusAndUser(NotificationStatus status,User user);

    List<Notification> findAllByUserOrderByCreatedAtDesc(User user);
}
