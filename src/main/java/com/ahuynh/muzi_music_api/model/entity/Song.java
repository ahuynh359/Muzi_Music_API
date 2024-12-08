package com.ahuynh.muzi_music_api.model.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.*;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @JsonIgnore
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "song_singer",
            joinColumns = {@JoinColumn(name = "song_id")},
            inverseJoinColumns = {@JoinColumn(name = "singer_id")})
    private Set<Singer> singers = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "song_type"
            , joinColumns = @JoinColumn(name = "song_id", referencedColumnName = "id")
            , inverseJoinColumns = @JoinColumn(name = "type_id", referencedColumnName = "id"))
    private Set<Type> types = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Listen> listens;

    @Column(name = "`key`")
    private Float key;

    private Float danceability;

    @Column(name = "`mode`")
    private Float mode;
    
    private Float loudness;
    
    private Float energy;
    
    private Float acousticness;
    
    private Float tempo;
    
    private Float instrumentalness;
    
    private Float valence;
    
    private Float liveness;
    
    private Float speechiness;

    @Column(name = "`time_signature`")
    private Float timeSignature;
    @Column(name = "`track_popularity`")
    private Float trackPopularity;

    public Song(String name, String avatar, String file, String lyrics, Album album) {
        this.name = name;
        this.avatar = avatar;
        this.file = file;
        this.lyrics = lyrics;
        this.album = album;
    }

    public Song(String name, String avatar, String file, String lyrics, Album album, Set<Singer> singers, Set<Type> types) {
        this.name = name;
        this.avatar = avatar;
        this.file = file;
        this.lyrics = lyrics;
        this.album = album;
        this.singers = singers;
        this.types = types;
    }


}
