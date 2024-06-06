package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.service.AlbumService;
import com.ahuynh.muzi_music_api.service.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    private final ObjectMapper objectMapper;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addAlbum(@RequestParam("avatar") MultipartFile avatar,
                                      @RequestParam("name") String name,
                                      @RequestParam("description") String description) {

        Album albumResponse = albumService.save(avatar, name, description);
        return new ResponseEntity<>(new ApiResponse(true, "Create Album Successfully", objectMapper.convertValue(albumResponse, Album.class)), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAlbum(@PathVariable(name = "id") Long id) {
        Album album = albumService.getAlbum(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllAlbum() {
        List<Album> album = albumService.getAlbum();
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", album), HttpStatus.OK);
    }

    @GetMapping("/songs/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getSongFromAlbum(@PathVariable(name = "id") Long id) {
        List<Song> songs = albumService.getSongFromAlbum(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", songs), HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAlbum(@PathVariable(name = "id") Long id) {
        albumService.deleteAlbum(id);
        return new ResponseEntity<>(new ApiResponse(true, "Delete Successfully", ""), HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateAlbum(@PathVariable(name = "id") Long id, @RequestBody AlbumRequest newAlbum) {
        Album album = albumService.updateAlbum(id, newAlbum);
        return new ResponseEntity<>(new ApiResponse(true, "Update Successfully", album), HttpStatus.OK);
    }


}