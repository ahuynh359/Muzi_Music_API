package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.config.security.CurrentUser;
import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.SingerService;
import com.ahuynh.muzi_music_api.utils.SortName;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/singer")
@RequiredArgsConstructor
public class SingerController {

    private final SingerService singerService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createSinger(@RequestParam String name, @RequestParam MultipartFile avatar) {
        return new ResponseEntity<>(new ApiResponse("Create Singer Successfully",
                singerService.createSinger(name, avatar)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    public ResponseEntity<?> getSingerById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Get Singer Successfully", singerService.getSingerById(id)), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteSinger(@PathVariable(name = "id") Long id) {
        singerService.deleteSinger(id);
        return new ResponseEntity<>(new MessageResponse("Delete Singer Successfully"), HttpStatus.OK);
    }


    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')  ")
    public ResponseEntity<?> updateSinger(@RequestParam Long id, @RequestParam String name, @RequestParam MultipartFile avatar) {
        return new ResponseEntity<>(new ApiResponse(" Update Singer Successfully",
                singerService.updateSinger(id, name, avatar)), HttpStatus.OK);
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    public ResponseEntity<?> getAllSingers(@RequestParam(value = "sort", required = false, defaultValue = "NEW") SortName sort) {
        return new ResponseEntity<>(new ApiResponse("Get All Singers Successfully", singerService.getAllSingers(sort)), HttpStatus.OK);
    }

    @GetMapping("/love")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    public ResponseEntity<?> getLoveSingersOfUser(@CurrentUser CustomUserDetail currentUser) {
        return new ResponseEntity<>(new ApiResponse("Get Love Singers Successfully", singerService.getLoveSingersOfUser(currentUser)), HttpStatus.OK);
    }



    @GetMapping("/{id}/songs")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    public ResponseEntity<?> getSongsFromSinger(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Success", singerService.getSongsFromSinger(id)), HttpStatus.OK);
    }

    @PostMapping("/love-or-unlove/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> loveOrUnloveSinger(@PathVariable(name = "id") Long id, @CurrentUser CustomUserDetail currentUser) {
        singerService.loveOrUnloveSinger(id, currentUser);
        return new ResponseEntity<>(new MessageResponse("Successfully"), HttpStatus.OK);
    }

    @GetMapping("/love/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> isUserLoveSinger(@CurrentUser CustomUserDetail currentUser, @PathVariable Long id) {
        return new ResponseEntity<>(new ApiResponse("Successfully", singerService.isUserLoveSinger(currentUser, id)), HttpStatus.OK);
    }


}
