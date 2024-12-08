package com.ahuynh.muzi_music_api.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "singer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Singer extends DateAudit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String avatar = "https://firebasestorage.googleapis.com/v0/b/muzimusic-c2598.appspot.com/o/avatar%2Fsinger.png?alt=media&token=88843039-e083-4713-befd-18e19c365536";

    @JsonIgnore
    @ManyToMany(mappedBy = "singers")
    private Set<Song> songs = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long followers;

    private int popularity;


    public Singer(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }


    public Singer(String name, String description, String avatar, Long followers, int popularity) {
        this.description = description;
        this.name = name;
        this.avatar = avatar;
        this.followers = followers;
        this.popularity = popularity;
    }

}

