package com.ahuynh.muzi_music_api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalResponse {
    private String totalSong;
    private String totalAlbum;
    private String totalUser;
    private String totalType;
    private String totalSinger;
}
