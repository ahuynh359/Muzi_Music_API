package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.TypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/type")
@RequiredArgsConstructor
public class TypeController {

    private final TypeService typeService;


    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addType(@RequestParam String name , @RequestPart MultipartFile avatar) {

        return new ResponseEntity<>(new ApiResponse("Create Type Successfully",
                typeService.createType(name,avatar)), HttpStatus.CREATED);
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteType(@PathVariable(name = "id") Long id) {
        typeService.deleteType(id);
        return new ResponseEntity<>(new MessageResponse("Delete Type Successfully"), HttpStatus.OK);
    }


    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateType(@RequestParam Long id, @RequestParam(required = false)  String name, @RequestPart(required = false)  MultipartFile avatar) {
        return new ResponseEntity<>(new ApiResponse(" Update Type Successfully",
                typeService.updateType(id,name,avatar)), HttpStatus.OK);
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllType() {
        return new ResponseEntity<>(new ApiResponse("Get All Types Successfully", typeService.getAllType()), HttpStatus.OK);
    }


    @GetMapping("/{id}/songs")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getSongsFromType(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Get Songs From Type Successfully", typeService.getSongFromType(id)), HttpStatus.OK);
    }



}