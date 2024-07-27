package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.config.security.CurrentUser;
import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.payload.request.UpdateSongRequest;
import com.ahuynh.muzi_music_api.utils.SortName;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    public ResponseEntity<?> getAllSongs(@RequestParam(value = "sort", required = false, defaultValue = "NEW") SortName sort) {
        return new ResponseEntity<>(new ApiResponse("Get All Songs Successfully", songService.getAllSong(sort)), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getSongById(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiResponse("Get Songs Successfully", songService.getSongById(id)), HttpStatus.OK);
    }

    @GetMapping("/top10")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getTop10Songs() {
        return new ResponseEntity<>(new ApiResponse("Get Top 10 Songs Successfully", songService.getTop10Songs()), HttpStatus.OK);
    }



    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> search(@RequestParam(value = "query") String query) {
        return new ResponseEntity<>(new ApiResponse("Search Successfully", songService.search(query)), HttpStatus.OK);
    }

    @PostMapping("/love/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> loveOrUnloveSong(@PathVariable(name = "id") Long id,@CurrentUser CustomUserDetail currentUser) {
        songService.loveOrUnloveSong(id,currentUser);
        return new ResponseEntity<>(new MessageResponse("Successfully"), HttpStatus.OK);
    }

    @GetMapping("/love")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getLoveSongs(@CurrentUser CustomUserDetail currentUser) {
        return new ResponseEntity<>(new ApiResponse("Successfully", songService.getLoveSongs(currentUser)), HttpStatus.OK);
    }

    @GetMapping("/is-love-song/{songId}")
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

    @PostMapping("/listen/{id}")
    public ResponseEntity<?> listenToSong(@CurrentUser CustomUserDetail currentUser, @PathVariable Long id) {
        songService.listenToSong(currentUser, id);
        return new ResponseEntity<>(new MessageResponse("Listen Successfully"), HttpStatus.OK);
    }

    @PutMapping("/avatar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> updateAvatar(@PathVariable Long id,
                                          @RequestPart("avatar") MultipartFile avatar) {

        return new ResponseEntity<>(new ApiResponse("Change Avatar Successfully",
                songService.updateAvatar( id, avatar)), HttpStatus.OK);
    }

    @PutMapping("/music/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> uploadMusic(@PathVariable Long id,
                                          @RequestPart("music") MultipartFile music) {

        return new ResponseEntity<>(new ApiResponse("Change Avatar Successfully",
                songService.uploadMusic( id, music)), HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> updateSong( @RequestBody UpdateSongRequest updateSongRequest) {

        return new ResponseEntity<>(new ApiResponse("Update Song Successfully",
                songService.updateSong(updateSongRequest)), HttpStatus.OK);
    }



}
