package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.config.security.CurrentUser;
import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.payload.request.AddSongRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> createSong(@RequestParam("name") String name,
                                        @RequestPart("avatar") MultipartFile avatar,
                                        @RequestPart("file") MultipartFile file,
                                        @RequestParam("lyrics") String lyrics,
                                        @RequestParam("albumId") Long albumId,
                                        @RequestParam("singerId") Set<Long> singerId,
                                        @RequestParam("typeId") Set<Long> typeId) {

        return new ResponseEntity<>(new ApiResponse("Create Song Successfully",
                songService.createSong(name, avatar, file, lyrics, albumId, singerId, typeId)), HttpStatus.OK);
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllSongs() {
        return new ResponseEntity<>(new ApiResponse("Get All Songs Successfully", songService.getAllSong()), HttpStatus.OK);
    }


    @GetMapping("/new")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getNewSongs() {
        return new ResponseEntity<>(new ApiResponse("Get New Songs Successfully", songService.getNewSongs()), HttpStatus.OK);
    }


    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> search(@RequestParam(value = "query") String query) {
        return new ResponseEntity<>(new ApiResponse("Search Successfully", songService.search(query)), HttpStatus.OK);
    }

    @PostMapping("/love-or-unlove/{songId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> loveOrUnloveSong(@CurrentUser CustomUserDetail currentUser, @PathVariable(name = "songId") Long songId) {
        songService.loveOrUnloveSong(currentUser, songId);
        return new ResponseEntity<>(new MessageResponse("Successfully"), HttpStatus.OK);
    }

    @GetMapping("/love-songs")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getLoveSongs(@CurrentUser CustomUserDetail currentUser) {
        return new ResponseEntity<>(new ApiResponse("Successfully", songService.getLoveSongs(currentUser)), HttpStatus.OK);
    }

    @GetMapping("/is-love-song{songId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> isUserLoveSong(@CurrentUser CustomUserDetail currentUser, @PathVariable Long songId) {
        return new ResponseEntity<>(new ApiResponse("Successfully", songService.isUserLoveSong(currentUser, songId)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return new ResponseEntity<>(new MessageResponse("Successfully"), HttpStatus.OK);
    }


}
