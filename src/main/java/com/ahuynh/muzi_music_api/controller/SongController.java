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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> addSong(@RequestParam("name") String name,
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
     * USER - ADMIN - EMPLOYEE
     */

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> getSongById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Successfully", songService.getSongById(id)), HttpStatus.OK);
    }

    /**
     * Lấy het song
     * USER - ADMIN - EMPLOYEE
     * List<SongDto>
     */

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllSong() {
        return new ResponseEntity<>(new ApiResponse("Successfully", songService.getAllSong()), HttpStatus.OK);
    }


    /**
     * delete song
     * ADMIN - EMPLOYEE
     * MessageResponse
     */

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return new ResponseEntity<>(new MessageResponse("Successfully"), HttpStatus.OK);
    }


}
