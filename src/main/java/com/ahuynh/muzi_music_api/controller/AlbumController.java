package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.payload.request.UpdateAlbumRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.AlbumService;
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

    /**
     * Thêm Album
     * Admin
     * AlbumDto
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addAlbum(@RequestPart("avatar") MultipartFile avatar,
                                      @RequestParam("name") String name
    ) {
        return new ResponseEntity<>
                (new ApiResponse("Create Success",
                        albumService.createAlbum(avatar, name)), HttpStatus.CREATED);
    }

    /**
     * Delete Album
     * ADMIN
     * MessageResponse
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAlbum(@PathVariable(name = "id") Long id) {
        albumService.deleteAlbum(id);
        return new ResponseEntity<>(new MessageResponse("Delete Success"), HttpStatus.OK);
    }


    /**
     * Lấy album theo id
     * ADMIN - USER
     * AlbumDto
     */
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAlbumById(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiResponse("Success", albumService.getAlbumById(id)), HttpStatus.OK);
    }

    /**
     * Lấy hết album
     * ADMIN - USER
     * List<AlbumDto>
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllAlbum() {
        return new ResponseEntity<>(new ApiResponse("Success", albumService.getAllAlbum()), HttpStatus.OK);
    }


    /**
     * Lấy song từ album theo id
     * ADMIN - USER
     * AlbumDto
     */
    @GetMapping("/{id}/songs")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getSongsFromAlbum(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Success",
                albumService.getSongsFromAlbum(id)), HttpStatus.OK);
    }


    /**
     * Update Alum - without avatar
     * ADMIN
     * AlbumDto
     */
    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateAlbum(@RequestBody UpdateAlbumRequest request) {
        return new ResponseEntity<>(new ApiResponse("Update Success", albumService.updateAlbum(request)), HttpStatus.OK);
    }

    /**
     * Update avatar
     * ADMIN
     * AlbumDto
     */
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateAlbum(@RequestParam Long id, @RequestPart MultipartFile avatar) {
        return new ResponseEntity<>(new ApiResponse("Update Success", albumService.updateAvatar(id, avatar)), HttpStatus.OK);
    }


}