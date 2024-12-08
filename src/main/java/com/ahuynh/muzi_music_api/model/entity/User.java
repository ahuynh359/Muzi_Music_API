package com.ahuynh.muzi_music_api.model.entity;

import com.ahuynh.muzi_music_api.model.entity.notification.Notification;
import com.ahuynh.muzi_music_api.model.entity.role.Role;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User extends DateAudit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(name = "hash_password")
    @Size(min = 6, message = "Password has at least 6 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String hashPassword;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    private String avatar = "https://firebasestorage.googleapis.com/v0/b/muzimusic-c2598.appspot.com/o/avatar%2Favatar.png?alt=media&token=0f4445e9-50a5-4425-9f7c-6c3012b0bcee";

    private boolean locked = false;

    @Column(name = "device_token")
    private String deviceToken = "erAxKAnqQnC5sgxv6RbkNs:APA91bEnItKurZcr3gEZAUxLOgVsoyTKx0sMTOBXf-tFcxbGH-X0XamgKf1vAWmWO5q41xrH_1vSJRmK931sFeOsfZle7IQt5IXGUqOR2xpnkPRQBSayry-HOrunf1nyt4NoXPZIfLXU";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Playlist> playlists = new HashSet<>();


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<Listen> listens = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private VerificationToken verificationToken;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_love_song", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "song_id", referencedColumnName = "id"))
    private Set<Song> loveSongs = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_love_singer", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "singer_id", referencedColumnName = "id"))
    private Set<Singer> loveSingers = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();

    public User(String email, String hashPassword, String username, Role role, String avatar) {
        this.email = email;
        this.hashPassword = hashPassword;
        this.username = username;
        this.role = role;
        this.avatar = avatar;


    }


    public User(String email, String hashPassword, String username, Role role) {
        this.email = email;
        this.hashPassword = hashPassword;
        this.username = username;
        this.role = role;


    }

    public void addLovedSong(Song song) {
        loveSongs.add(song);
    }

    public void removeLoveSong(Song song) {
        loveSongs.remove(song);
    }

    public void removeLoveSinger(Singer singer) {
        loveSingers.remove(singer);
    }

    public void addLoveSinger(Singer singer) {
        loveSingers.add(singer);
    }


}


