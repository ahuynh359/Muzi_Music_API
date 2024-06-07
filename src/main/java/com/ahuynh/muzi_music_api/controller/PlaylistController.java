package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Playlist;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import com.ahuynh.muzi_music_api.payload.request.PlaylistRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.PlaylistResponse;
import com.ahuynh.muzi_music_api.payload.response.SongResponse;
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
    public ResponseEntity<?> addPlaylistByUser(@Valid @RequestBody PlaylistRequest playlistRequest) {

        playlistService.addPlaylistByUser(playlistRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Create  Successfully",
                ""), HttpStatus.CREATED);
    }

    @GetMapping("/get-by-id/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getPlaylistById(@PathVariable(name = "id") Long id) {
        Playlist playlist = playlistService.getPlaylistById(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", objectMapper.convertValue(playlist, PlaylistResponse.class)), HttpStatus.OK);
    }

    @GetMapping("/get-by-name")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getPlaylistByNameAndUser(@RequestParam(name = "name") String name, @RequestParam(name = "userId") Long userId) {
        Playlist playlist = playlistService.getPlaylistByNameAndUser(name, userId);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", objectMapper.convertValue(playlist, PlaylistResponse.class)), HttpStatus.OK);
    }

    @GetMapping("/get-all/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllPlaylistByUser(@PathVariable(name = "userId") Long userId) {
        List<Playlist> playlist = playlistService.getAllPlaylistByUser(userId);
        List<PlaylistResponse> responses = PlaylistResponse.toResponseList(playlist);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", responses), HttpStatus.OK);
    }


    //Ko xoa dươc why ?
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> deletePlaylistById(@PathVariable(name = "id") Long id) {
        playlistService.deletePlaylist(id);
        return new ResponseEntity<>(new ApiResponse(true, "Delete  Successfully", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updatePlaylist(@PathVariable(name = "id") Long id, @RequestBody PlaylistRequest playlistRequest) {
        Playlist playlist = playlistService.updatePlaylist(id, playlistRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Update  Successfully", objectMapper.convertValue(playlist, PlaylistResponse.class)), HttpStatus.OK);
    }


    @PostMapping("/add-song/{songId}/{playlistId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> addSongToPlaylist(@PathVariable(name = "songId") Long songId, @PathVariable(name = "playlistId") Long playlistId) {

        playlistService.addSongToPlaylist(songId, playlistId);
        return new ResponseEntity<>(new ApiResponse(true, "  Successfully",
                ""), HttpStatus.CREATED);
    }

    @PostMapping("/remove-song/{songId}/{playlistId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> removeSongFromPlaylist(@PathVariable(name = "songId") Long songId, @PathVariable(name = "playlistId") Long playlistId) {

        playlistService.deleteSongFromPlaylist(songId, playlistId);
        return new ResponseEntity<>(new ApiResponse(true, "  Successfully",
                ""), HttpStatus.CREATED);
    }


    @GetMapping("/get-all-song/{playlistId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllSongFromPlaylist(@PathVariable(name = "playlistId") Long playlistId) {
        List<Song> song = playlistService.getAllSongFromPlaylist(playlistId);
        List<SongResponse> responses = SongResponse.toResponseList(song);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", responses), HttpStatus.OK);
    }


}