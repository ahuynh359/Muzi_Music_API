package com.ahuynh.muzi_music_api.payload.response;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Playlist;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PlaylistResponse {
    private Long id;
    private String name;
    private UserResponse users;
    private List<SongResponse> songs = new ArrayList<>();
    private Instant createdAt;


    public static List<PlaylistResponse> toResponseList(List<Playlist> playlists) {
        return playlists.stream()
                .map(PlaylistResponse::toResponse)
                .collect(Collectors.toList());


    }

    private static PlaylistResponse toResponse(Playlist playlist) {
        PlaylistResponse response = new PlaylistResponse();
        response.setId(playlist.getId());
        response.setName(playlist.getName());
        response.setCreatedAt(playlist.getCreatedAt());
        response.setUsers(UserResponse.toUserResponse(playlist.getUser()));
        List<SongResponse> songResponses = new ArrayList<>();
        for (Song song : playlist.getSongs()) {
            songResponses.add(SongResponse.toResponse(song));
        }
        response.setSongs(songResponses);
        return response;
    }
}
