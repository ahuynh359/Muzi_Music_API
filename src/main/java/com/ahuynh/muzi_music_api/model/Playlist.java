package com.ahuynh.muzi_music_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlist")
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"created_at"},
        allowGetters = true
)
@NoArgsConstructor
@AllArgsConstructor
public class Playlist  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true)
    private String name;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "song_playlist"
            , joinColumns = @JoinColumn(name = "playlist_id", referencedColumnName = "id")
            , inverseJoinColumns = @JoinColumn(name = "song_id", referencedColumnName = "id"))
    private List<Song> songs = new ArrayList<>();

    public Playlist(String name, User user) {
        this.name = name;
        this.user = user;
    }
}