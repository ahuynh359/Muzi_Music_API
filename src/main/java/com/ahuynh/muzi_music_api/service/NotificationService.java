package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.exception.CustomException;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.NotificationDto;
import com.ahuynh.muzi_music_api.model.entity.notification.Notification;
import com.ahuynh.muzi_music_api.model.entity.notification.NotificationStatus;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.mapper.NotificationMapper;
import com.ahuynh.muzi_music_api.payload.response.CountUnreadNotificationResponse;
import com.ahuynh.muzi_music_api.repository.NotificationRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    public List<NotificationDto> getAllNotifications(CustomUserDetail customUserDetail) {
        User user = userRepository.findById(customUserDetail.getId()).orElseThrow(() -> new EntityNotFoundException("No User Found"));
        return notificationMapper.convertToDtoList(notificationRepository.findAllByUserOrderByCreatedAtDesc(user));
    }

    public NotificationDto getNotificationById(Long id) {
        return notificationMapper.convertToDto(notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Notification Found")));
    }


    public void deleteNotification(Long id, CustomUserDetail customUserDetail) {
        User user = userRepository.findById(customUserDetail.getId()).orElseThrow(() -> new EntityNotFoundException("No User Found"));
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Notification Found"));
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new CustomException("You are not allowed to delete this notification");
        }
        notificationRepository.deleteById(id);
    }

    public void deleteAllNotifications(CustomUserDetail customUserDetail) {
        User user = userRepository.findById(customUserDetail.getId()).orElseThrow(() -> new EntityNotFoundException("No User Found"));
        List<Notification> notifications = notificationRepository.findAllByUser(user);
        notificationRepository.deleteAll(notifications);
    }

    public NotificationDto markNotificationAsRead(Long id, CustomUserDetail customUserDetail) {
        User user = userRepository.findById(customUserDetail.getId()).orElseThrow(() -> new EntityNotFoundException("No User Found"));
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No Notification Found"));
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new CustomException("You are not allowed to mark this notification");
        }
        if(notification.getStatus() == NotificationStatus.READ) {
            notification.setStatus(NotificationStatus.NOT_READ);
        } else
            notification.setStatus(NotificationStatus.READ);

        return notificationMapper.convertToDto(notificationRepository.save(notification));
    }

    public List<NotificationDto> markAllNotificationsAsRead(CustomUserDetail customUserDetail) {
        User user = userRepository.findById(customUserDetail.getId()).orElseThrow(() -> new EntityNotFoundException("No User Found"));
        List<Notification> notifications = notificationRepository.findAllByUser(user);
        for (Notification notification : notifications) {
            notification.setStatus(NotificationStatus.READ);
        }
        return notificationMapper.convertToDtoList(notificationRepository.saveAll(notifications));
    }

    public CountUnreadNotificationResponse countUnreadNotification(CustomUserDetail customUserDetail) {
        User user = userRepository.findById(customUserDetail.getId()).orElseThrow(() -> new EntityNotFoundException("No User Found"));
        List<Notification> notifications = notificationRepository.findAllByStatusAndUser(NotificationStatus.NOT_READ, user);
        return new CountUnreadNotificationResponse(notifications.size());
    }
}