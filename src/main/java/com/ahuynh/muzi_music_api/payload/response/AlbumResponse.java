package com.ahuynh.muzi_music_api.payload.response;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.N;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumResponse {
    private Long id;
    private String name;
    private String description;
    private String avatar;
    private Instant createdAt;

    public static AlbumResponse toResponse(Album album) {
        AlbumResponse response = new AlbumResponse();
        response.id = album.getId();
        response.description = album.getDescription();
        response.avatar = album.getAvatar();
        response.name = album.getName();
        response.createdAt = album.getCreatedAt();
        return response;
    }

    public static List<AlbumResponse> toResponseList(List<Album> albums) {
        return albums.stream()
                .map(AlbumResponse::toResponse)
                .collect(Collectors.toList());


    }
}
