package com.ahuynh.muzi_music_api.model.dto;

import com.ahuynh.muzi_music_api.model.entity.*;
import com.ahuynh.muzi_music_api.model.entity.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String username;
    private String avatar;
    private boolean locked;
    private RoleDto role;
    private String createdAt;
    private String updatedAt;

}
