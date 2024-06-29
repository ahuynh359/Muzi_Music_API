package com.ahuynh.muzi_music_api.payload.request;

import lombok.Data;

@Data
public class AddPlaylistRequest {
    private Long userId;
    private String name;
}
