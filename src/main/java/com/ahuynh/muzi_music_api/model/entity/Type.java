package com.ahuynh.muzi_music_api.model.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Type extends DateAudit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    private String avatar = "https://firebasestorage.googleapis.com/v0/b/muzimusic-c2598.appspot.com/o/app%2Falbum_2.png?alt=media&token=e1ac5e9f-f581-427d-9da2-9a40d31b9dda";


    @ManyToMany(mappedBy = "types", fetch = FetchType.LAZY)
    private Set<Song> songs = new HashSet<>();

    public Type(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }


}