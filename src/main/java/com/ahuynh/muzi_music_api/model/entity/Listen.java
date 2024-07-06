package com.ahuynh.muzi_music_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "listen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Listen extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;


    public Listen(User user, Song song) {
        this.user = user;
        this.song = song;
    }

}