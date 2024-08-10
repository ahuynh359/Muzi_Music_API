package com.ahuynh.muzi_music_api.model.entity.notification;

import com.ahuynh.muzi_music_api.model.entity.Comment;
import com.ahuynh.muzi_music_api.model.entity.DateAudit;
import com.ahuynh.muzi_music_api.model.entity.Song;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Notification extends DateAudit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.NOT_READ;

    @Enumerated(EnumType.STRING)
    private NotificationType type = NotificationType.SONG;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
   private Song song;





    public Notification(String title, String content,  User user,NotificationType type, Song song) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.type = type;
        this.song = song;

    }


}