package com.ahuynh.muzi_music_api.payload.response;

import com.ahuynh.muzi_music_api.model.dto.SongDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongListenResponse {
    private SongDto song;
    private List<ListenOfDayResponse> listenDetail;
}
