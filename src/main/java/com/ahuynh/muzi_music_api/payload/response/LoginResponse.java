package com.ahuynh.muzi_music_api.payload.response;

import com.ahuynh.muzi_music_api.model.dto.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String jwt;
    private boolean isAdmin;
}