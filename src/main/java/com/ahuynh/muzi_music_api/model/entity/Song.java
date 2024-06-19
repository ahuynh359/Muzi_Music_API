package com.ahuynh.muzi_music_api.model.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "song")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Song extends DateAudit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    private String avatar = "https://firebasestorage.googleapis.com/v0/b/muzimusic-c2598.appspot.com/o/avatar%2Fsong.png?alt=media&token=5506be43-2a8c-41d9-a276-ad9722f92725";
    private String file;

    @Column(columnDefinition = "TEXT")
    private String lyrics;

    private Long listen = 0L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @JsonIgnore
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    @JsonIgnore
    @ManyToMany(mappedBy = "songs")
    private Set<Singer> singers = new HashSet<>();

    @ManyToMany(mappedBy = "songs")
    private Set<Type> types = new HashSet<>();

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Listen> listens;

    public Song(String name, String avatar, String file, String lyrics, Album album) {
        this.name = name;
        this.avatar = avatar;
        this.file = file;
        this.lyrics = lyrics;
        this.album = album;
    }

    public void addSinger(Singer singer) {
        singers.add(singer);
    }

    public void removeSinger(Singer singer) {
        singers.remove(singer);
    }


}
