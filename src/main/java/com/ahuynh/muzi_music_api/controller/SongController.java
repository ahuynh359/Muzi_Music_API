package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import com.ahuynh.muzi_music_api.payload.request.SongRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.service.AlbumService;
import com.ahuynh.muzi_music_api.service.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;
    private final ObjectMapper objectMapper;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addSong(@RequestParam("name") String name,
                                     @RequestParam("avatar") MultipartFile avatar,
                                     @RequestParam("file") MultipartFile file,
                                     @RequestParam("lyrics") String lyrics,
                                     @RequestParam("albumId") Long albumId) {

        Song song = songService.save(name,avatar,file,lyrics,albumId);
        return new ResponseEntity<>(new ApiResponse(true, "Create Song Successfully",
                objectMapper.convertValue(song, Song.class)), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getSong(@PathVariable(name = "id") Long id) {
        Song song = songService.getSong(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", objectMapper.convertValue(song, Song.class)), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteSong(@PathVariable(name = "id") Long id) {
        songService.deleteSong(id);
        return new ResponseEntity<>(new ApiResponse(true, "Delete song Successfully", null), HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateSong(@PathVariable(name = "id") Long id, @RequestBody SongRequest newSong) {
        Song song = songService.updateSong(id, newSong);
        return new ResponseEntity<>(new ApiResponse(true, "Update song Successfully",
                objectMapper.convertValue(song, Song.class)), HttpStatus.OK);
    }


}