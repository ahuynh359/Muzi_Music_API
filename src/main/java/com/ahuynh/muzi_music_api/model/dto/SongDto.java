package com.ahuynh.muzi_music_api.model.dto;

import com.ahuynh.muzi_music_api.model.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDto {

    private Long id;
    private String name;
    private String avatar;
    private String file;

    private String lyrics;
    private AlbumDto album;
    private Set<SingerDto> singers;
    private Set<TypeDto> types;
    private String createdAt;
    private String updatedAt;
}
