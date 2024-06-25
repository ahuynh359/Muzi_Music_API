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

    @ManyToMany(fetch = FetchType.LAZY,
            cascade=  CascadeType.ALL,
            mappedBy = "types")
    @JsonIgnore
    private Set<Song> songs = new HashSet<>();

    public Type(String name){
        this.name = name;
    }


}