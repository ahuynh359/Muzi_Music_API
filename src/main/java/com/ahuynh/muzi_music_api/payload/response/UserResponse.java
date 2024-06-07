package com.ahuynh.muzi_music_api.payload.response;

import com.ahuynh.muzi_music_api.model.Playlist;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.model.role.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String avatar;
    private boolean enabled;
    private Set<Role> role;
    private Instant createdAt;

    public static UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.id = user.getId();
        userResponse.email = user.getEmail();
        userResponse.username = user.getUsername();
        userResponse.avatar = user.getAvatar();
        userResponse.enabled = user.isEnabled();
        userResponse.role = user.getRole();
        userResponse.createdAt = user.getCreatedAt();
        return userResponse;

    }

    public static List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(UserResponse::toUserResponse)
                .collect(Collectors.toList());


    }
}
