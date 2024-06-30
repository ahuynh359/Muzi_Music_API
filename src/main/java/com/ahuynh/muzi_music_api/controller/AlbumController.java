package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.payload.request.UpdateAlbumRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addAlbum(@RequestParam String name, @RequestPart MultipartFile avatar
    ) {
        return new ResponseEntity<>
                (new ApiResponse("Create Album Successfully",
                        albumService.createAlbum(name, avatar)), HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAlbum(@PathVariable(name = "id") Long id) {
        albumService.deleteAlbum(id);
        return new ResponseEntity<>(new MessageResponse("Delete Success"), HttpStatus.OK);
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllAlbum() {
        return new ResponseEntity<>(new ApiResponse("Get All Albums Successfully", albumService.getAllAlbum()), HttpStatus.OK);
    }


    @GetMapping("/{id}/songs")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getSongsFromAlbum(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Get Songs From Album Successfully",
                albumService.getSongsFromAlbum(id)), HttpStatus.OK);
    }


    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateAlbum(@RequestBody UpdateAlbumRequest request) {
        return new ResponseEntity<>(new ApiResponse("Update Album Successfully", albumService.updateAlbum(request)), HttpStatus.OK);
    }


    @PutMapping("/avatar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateAvatarAlbum(@PathVariable Long id, @RequestPart MultipartFile avatar) {
        return new ResponseEntity<>(new ApiResponse("Update Album Successfully", albumService.updateAvatar(id, avatar)), HttpStatus.OK);
    }


}