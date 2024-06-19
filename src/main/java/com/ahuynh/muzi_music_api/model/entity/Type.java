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


    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "type_song"
            , joinColumns = @JoinColumn(name = "type_id", referencedColumnName = "id")
            , inverseJoinColumns = @JoinColumn(name = "song_id", referencedColumnName = "id"))
    private Set<Song> songs = new HashSet<>();


}