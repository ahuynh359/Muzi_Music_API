package com.ahuynh.muzi_music_api.model.dto;

import com.ahuynh.muzi_music_api.model.entity.notification.NotificationStatus;
import lombok.Data;

@Data
public class NotificationDto {
    private Long id;
    private String title;
    private String content;
    private NotificationStatus status;
    private String time;
    private UserDto user;
    private String createdAt;
    private String updatedAt;
}
