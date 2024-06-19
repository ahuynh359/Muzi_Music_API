package com.ahuynh.muzi_music_api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class LoginResponse implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String jwt;
}