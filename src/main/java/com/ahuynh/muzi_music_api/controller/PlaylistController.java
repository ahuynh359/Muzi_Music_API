package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Playlist;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import com.ahuynh.muzi_music_api.payload.request.PlaylistRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.service.AlbumService;
import com.ahuynh.muzi_music_api.service.PlaylistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final ObjectMapper objectMapper;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> addPlaylist(@Valid @RequestBody PlaylistRequest playlistRequest) {

        Playlist playlist = playlistService.save(playlistRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Create  Successfully", objectMapper.convertValue(playlist, Playlist.class)), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getPlaylist(@PathVariable(name = "id") Long id) {
        Playlist playlist = playlistService.getPlaylist(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", objectMapper.convertValue(playlist, Playlist.class)), HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllPlaylistByUserId() {
        List<Playlist> playlist = playlistService.getAllPlaylist();
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", playlist), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> deletePlaylist(@PathVariable(name = "id") Long id) {
        playlistService.deletePlaylist(id);
        return new ResponseEntity<>(new ApiResponse(true, "Delete  Successfully", null), HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updatePlaylist(@PathVariable(name = "id") Long id, @RequestBody PlaylistRequest playlistRequest) {
        Playlist playlist = playlistService.updatePlaylist(id, playlistRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Update  Successfully", objectMapper.convertValue(playlist, Playlist.class)), HttpStatus.OK);
    }


}