package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.model.Type;
import com.ahuynh.muzi_music_api.payload.request.TypeRequest;
import com.ahuynh.muzi_music_api.payload.request.UpdateSongRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.service.SongService;
import com.ahuynh.muzi_music_api.service.TypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/type")
@RequiredArgsConstructor
public class TypeController {

    private final TypeService typeService;
    private final ObjectMapper objectMapper;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addType(@Valid @RequestBody TypeRequest request) {

        Type type = typeService.save(request);
        return new ResponseEntity<>(new ApiResponse(true, "Create Successfully",
                objectMapper.convertValue(type, Type.class)), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getType(@PathVariable(name = "id") Long id) {
        Type type = typeService.getType(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", objectMapper.convertValue(type, Type.class)), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteType(@PathVariable(name = "id") Long id) {
        typeService.deleteType(id);
        return new ResponseEntity<>(new ApiResponse(true, "Delete Successfully", null), HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateSong(@PathVariable(name = "id") Long id, @RequestBody TypeRequest request) {
        Type type = typeService.updateType(id, request);
        return new ResponseEntity<>(new ApiResponse(true, "Update  Successfully",
                objectMapper.convertValue(type, Type.class)), HttpStatus.OK);
    }
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllType() {
        List<Type> type = typeService.getAllType();
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", type), HttpStatus.OK);
    }


    @GetMapping("/songs/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getSongFromType(@PathVariable(name = "id") Long id) {
        List<Song> songs = typeService.getSongFromType(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", songs), HttpStatus.OK);
    }



}