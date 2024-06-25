package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.SingerService;
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

    /**
     * Tao singer
     * ADMIN
     * SingerDto
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createSinger  (@RequestParam  String name, @RequestParam  MultipartFile avatar) {

        return new ResponseEntity<>(new ApiResponse("Success",
                singerService.createSinger(name, avatar)), HttpStatus.CREATED);
    }

    /**
     * Lay singer theo id
     * ADMIN - USER
     * SingerDto
     */
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    public ResponseEntity<?> getSingerById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Success", singerService.getSingerById(id)), HttpStatus.OK);
    }

    /**
     * Xoa singer
     * ADMIN
     * Message
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteSinger(@PathVariable(name = "id") Long id) {
        singerService.deleteSinger(id);
        return new ResponseEntity<>(new MessageResponse("Success"), HttpStatus.OK);
    }

    /**
     * Sua singer
     * ADMIN
     * SingerDto
     */
    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')  ")
    public ResponseEntity<?> updateSinger(@RequestParam Long id, @RequestParam String name, @RequestParam MultipartFile avatar) {
        return new ResponseEntity<>(new ApiResponse(" Success",
                singerService.updateSinger(id, name, avatar)), HttpStatus.OK);
    }

    /**
     * Lay all singer
     * ADMIN - USER
     * List<SingerDto>
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    public ResponseEntity<?> getAllSinger() {
        return new ResponseEntity<>(new ApiResponse("Success", singerService.getAllSinger()), HttpStatus.OK);
    }

    /**
     * Lay new singer
     * ADMIN - USER
     * List<SingerDto>
     */
    @GetMapping("/new")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    public ResponseEntity<?> getNewSingers() {
        return new ResponseEntity<>(new ApiResponse("Success", singerService.getNewSingers()), HttpStatus.OK);
    }

    /**
     * Lay tat ca song cua singer
     * ADMIN - USER
     * List<SingerDto>
     */
    @GetMapping("/{id}/songs")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    public ResponseEntity<?> getSongFromSinger(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Success", singerService.getSongFromSinger(id)), HttpStatus.OK);
    }


}
