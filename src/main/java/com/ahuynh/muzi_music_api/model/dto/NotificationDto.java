package com.ahuynh.muzi_music_api.model.dto;

import com.ahuynh.muzi_music_api.model.entity.notification.NotificationStatus;
import com.ahuynh.muzi_music_api.model.entity.notification.NotificationType;
import lombok.Data;

@Data
public class NotificationDto {
    private Long id;
    private String title;
    private String content;
    private NotificationType type;
    private NotificationStatus status;
    private Long songId;
    private Long commentId = null;
    private String time;
    private UserDto user;
    private String createdAt;
    private String updatedAt;
}
