package com.ahuynh.muzi_music_api.payload.response;

import com.ahuynh.muzi_music_api.model.dto.AlbumDto;
import com.ahuynh.muzi_music_api.model.dto.SingerDto;
import com.ahuynh.muzi_music_api.model.dto.SongDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoveSongResponse {
    private String total;
    private List<SongDto> songs;


}