package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.config.security.CurrentUser;
import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.model.dto.NotificationDto;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    @GetMapping("/all")
    public ResponseEntity<?> getAllNotifications(@CurrentUser CustomUserDetail customUserDetail) {
        List<NotificationDto> notifications = notificationService.getAllNotifications(customUserDetail);
        return new ResponseEntity<>(new ApiResponse("Retrieved all user notifications successfully", notifications), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable Long id) {
        NotificationDto notification = notificationService.getNotificationById(id);
        return new ResponseEntity<>(new ApiResponse("Retrieved notification successfully", notification), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    @GetMapping("/count")
    public ResponseEntity<?> countUnreadNotification(@CurrentUser CustomUserDetail customUserDetail) {

        return new ResponseEntity<>(new ApiResponse("Retrieved notification successfully", notificationService.countUnreadNotification(customUserDetail)), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id,@CurrentUser CustomUserDetail customUserDetail) {
        notificationService.deleteNotification(id,customUserDetail);
        return new ResponseEntity<>(new MessageResponse("Deleted notification successfully"), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    @DeleteMapping
    public ResponseEntity<?> deleteAllNotifications(@CurrentUser CustomUserDetail customUserDetail) {
        notificationService.deleteAllNotifications(customUserDetail);
        return new ResponseEntity<>(new MessageResponse("Deleted all notification successfully"), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long id,@CurrentUser CustomUserDetail customUserDetail) {
        NotificationDto notification = notificationService.markNotificationAsRead(id,customUserDetail);
        return new ResponseEntity<>(new ApiResponse("Marked notification as read successfully", notification), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    @PutMapping("/read-all")
    public ResponseEntity<?> markAllNotificationsAsRead(@CurrentUser CustomUserDetail customUserDetail) {
        List<NotificationDto> notifications = notificationService.markAllNotificationsAsRead(customUserDetail);
        return new ResponseEntity<>(new ApiResponse("Marked all notifications as read successfully", notifications), HttpStatus.OK);
    }
}