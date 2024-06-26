package com.ahuynh.muzi_music_api.controller;

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
                                     @RequestParam("avatar") MultipartFile avatar,
                                     @RequestParam("file") MultipartFile file,
                                     @RequestParam("lyrics") String lyrics,
                                     @RequestParam("albumId") Long albumId,
                                     @RequestParam("singerId") Set<Long> singerId,
                                     @RequestParam("typeId") Set<Long> typeId) {

        return new ResponseEntity<>(new ApiResponse("Success", songService.createSong(name, avatar, file, lyrics, albumId, singerId, typeId)), HttpStatus.OK);
    }

    /**
     * Lấy song by id
     * USER - ADMIN
     */

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    public ResponseEntity<?> getSongById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Successfully", songService.getSongById(id)), HttpStatus.OK);
    }

    /**
     * Lấy het song
     * USER - ADMIN
     * List<SongDto>
     */

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllSongs() {
        return new ResponseEntity<>(new ApiResponse("Successfully", songService.getAllSong()), HttpStatus.OK);
    }

    /**
     * Lấy new song
     * USER - ADMIN
     * List<SongDto>
     */

    @GetMapping("/new")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getNewSongs() {
        return new ResponseEntity<>(new ApiResponse("Successfully", songService.getNewSongs()), HttpStatus.OK);
    }

    /**
     * Search song
     * USER - ADMIN
     * List<SongDto>
     * List<AlbumDto>
     * List<SingerDto>
     */

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> search(@RequestParam(value = "query") String query) {
        return new ResponseEntity<>(new ApiResponse("Successfully", songService.search(query)), HttpStatus.OK);
    }

    @PostMapping("/love/{userId}/{songId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> loveSong(@PathVariable(name = "userId") Long userId, @PathVariable(name = "songId") Long songId) {
        songService.loveSong(userId, songId);
        return new ResponseEntity<>(new MessageResponse("Successfully"), HttpStatus.OK);
    }



    @PostMapping("/unlove/{userId}/{songId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> unloveSong(@PathVariable(name = "userId") Long userId, @PathVariable(name = "songId") Long songId) {
        songService.unloveSong(userId, songId);
        return new ResponseEntity<>(new MessageResponse("Successfully"), HttpStatus.OK);
    }

    @GetMapping("/love/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getLoveSong(@PathVariable(name = "userId") Long userId) {
        return new ResponseEntity<>(new ApiResponse("Successfully", songService.getLoveSongByUserId(userId)), HttpStatus.OK);
    }

    @GetMapping("/is-love-song/{userId}/{songId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> isUserLoveSong(@PathVariable(name = "userId") Long userId,@PathVariable Long songId) {
        return new ResponseEntity<>(new ApiResponse("Successfully", songService.isUserLoveSong(userId,songId)), HttpStatus.OK);
    }



    /**
     * delete song
     * ADMIN
     * MessageResponse
     */

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return new ResponseEntity<>(new MessageResponse("Successfully"), HttpStatus.OK);
    }


}
