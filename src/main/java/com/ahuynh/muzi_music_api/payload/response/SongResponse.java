package com.ahuynh.muzi_music_api.payload.response;

import com.ahuynh.muzi_music_api.model.*;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class SongResponse {
    private Long id;
    private String name;
    private String avatar;
    private String file;
    private String lyrics;
    private AlbumResponse album;
    private Long listen;
    private String singer;
    private List<TypeResponse> types = new ArrayList<>();

    public static SongResponse toResponse(Song song) {
        SongResponse response = new SongResponse();
        response.id = song.getId();
        response.name = song.getName();
        response.avatar = song.getAvatar();
        response.file = song.getFile();
        response.lyrics = song.getLyrics();
        response.album = AlbumResponse.toResponse(song.getAlbum());
        response.listen = song.getListen();
        response.singer = song.getSinger();

        List<TypeResponse> typeResponse = new ArrayList<>();
        song.getTypes().forEach(type -> {
            typeResponse.add(TypeResponse.toResponse(type));
        });
        response.types = typeResponse;
        return response;


    }

    public static List<SongResponse> toResponseList(List<Song> songs) {
        return songs.stream()
                .map(SongResponse::toResponse)
                .collect(Collectors.toList());


    }

    public static Set<SongResponse> toResponseSet(Set<Song> songs) {
        return songs.stream()
                .map(SongResponse::toResponse)
                .collect(Collectors.toSet());
    }
}
