package com.ahuynh.muzi_music_api.model.entity;

import com.ahuynh.muzi_music_api.model.entity.notification.Notification;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Comment extends DateAudit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;
    @JsonManagedReference

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment commentParent;

    @JsonBackReference
    @OneToMany(mappedBy = "commentParent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> replies = new HashSet<>();




    public Comment(User user, Song song, String content) {
        this.user = user;
        this.song = song;
        this.content = content;

    }

    public Comment(User user, Song song, String content, Comment commentParent) {
        this.user = user;
        this.song = song;
        this.content = content;
        this.commentParent = commentParent;

    }


    public void addReply(Comment reply) {
        replies.add(reply);

    }
}

