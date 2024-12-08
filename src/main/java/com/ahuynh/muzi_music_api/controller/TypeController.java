package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.dto.SongDto;
import com.ahuynh.muzi_music_api.model.dto.TypeDto;
import com.ahuynh.muzi_music_api.payload.request.UpdateTypeRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.TypeService;
import com.ahuynh.muzi_music_api.utils.SortName;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/type")
@RequiredArgsConstructor
public class TypeController {

    private final TypeService typeService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addType(@RequestParam String name , @RequestPart MultipartFile avatar) {

        return new ResponseEntity<>(new ApiResponse("Create Type Successfully",
                typeService.createType(name,avatar)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteType(@PathVariable(name = "id") Long id) {
        typeService.deleteType(id);
        return new ResponseEntity<>(new MessageResponse("Delete Type Successfully"), HttpStatus.OK);
    }


    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateType(@Valid @RequestBody UpdateTypeRequest request) {
        return new ResponseEntity<>(new ApiResponse(" Update Type Successfully",
                typeService.updateType(request)), HttpStatus.OK);
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllTypes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "NEW") SortName sort) {

        Pageable pageable = PageRequest.of(page, size);
        List<TypeDto> typesPage = typeService.getAllTypes(sort, pageable);

        return new ResponseEntity<>(new ApiResponse("Get All Types Successfully", typesPage), HttpStatus.OK);
    }


    @PutMapping("/avatar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> updateAvatar(@PathVariable Long id,
                                          @RequestPart("avatar") MultipartFile avatar) {

        return new ResponseEntity<>(new ApiResponse("Change Avatar Successfully",
                typeService.updateAvatar( id, avatar)), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getTypeById(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiResponse("Get Type Successfully", typeService.getTypeById(id)), HttpStatus.OK);
    }


    @GetMapping("/{id}/songs")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getSongsFromType(
            @PathVariable(name = "id") Long id,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SongDto> songsPage = typeService.getSongsFromType(id, pageable);

        return new ResponseEntity<>(
                new ApiResponse("Get Songs From Type Successfully", songsPage),
                HttpStatus.OK
        );
    }




}