package com.ahuynh.muzi_music_api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MessageResponse implements Serializable {
    private String message;
}