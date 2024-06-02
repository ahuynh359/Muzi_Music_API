package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import com.ahuynh.muzi_music_api.service.AlbumService;
import com.ahuynh.muzi_music_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')" )
    public ResponseEntity<?> addAlbum(@RequestParam("image") MultipartFile image) {
        AlbumRequest albumRequest = new AlbumRequest("ACB","ADD","ABC");
//        albumService.save(albumRequest);
        return new ResponseEntity<>(albumRequest, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAlbum(@PathVariable(name = "id") Long id){
        Album album =  albumService.getAlbum(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')" )
    public ResponseEntity<?> deleteAlbum(@PathVariable(name = "id") Long id){
         albumService.deleteAlbum(id);
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }




}