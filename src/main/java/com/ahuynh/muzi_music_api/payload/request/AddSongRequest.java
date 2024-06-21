package com.ahuynh.muzi_music_api.payload.request;

import com.ahuynh.muzi_music_api.model.entity.Album;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSongRequest {
    private String name;
    private String file;
    private String lyrics;
    private Long albumId;
    private List<Long> singerId;
    private List<Long> typeId;
}
