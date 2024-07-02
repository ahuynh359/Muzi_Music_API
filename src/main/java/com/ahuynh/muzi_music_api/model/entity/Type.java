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

    private String avatar = "https://firebasestorage.googleapis.com/v0/b/muzimusic-c2598.appspot.com/o/app%2Ftype_1.png?alt=media&token=d07762bc-c6be-4f9a-bded-4a221cd057b0";

    @JsonIgnore
    @ManyToMany(mappedBy = "types")
    private Set<Song> songs = new HashSet<>();

    public Type(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }


}