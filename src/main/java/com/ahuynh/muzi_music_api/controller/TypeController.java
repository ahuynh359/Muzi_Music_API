package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.payload.request.TypeRequest;
import com.ahuynh.muzi_music_api.payload.request.UpdateTypeRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.TypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/type")
@RequiredArgsConstructor
public class TypeController {

    private final TypeService typeService;

    /**
     * Tao type
     * ADMIN
     * TypeDto
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addType(@Valid @RequestBody TypeRequest request) {

        return new ResponseEntity<>(new ApiResponse("Success",
                typeService.createType(request)), HttpStatus.CREATED);
    }

    /**
     * Lay type theo id
     * ADMIN - USER
     * TypeDto
     */
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getTypeById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Success", typeService.getTypeById(id)), HttpStatus.OK);
    }

    /**
     * Xoa delete
     * ADMIN
     * Message
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteType(@PathVariable(name = "id") Long id) {
        typeService.deleteType(id);
        return new ResponseEntity<>(new MessageResponse("Success"), HttpStatus.OK);
    }

    /**
     * Sua type
     * ADMIN
     * TypeDto
     */
    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateType(@RequestBody UpdateTypeRequest request) {
        return new ResponseEntity<>(new ApiResponse(" Success",
                typeService.updateType(request)), HttpStatus.OK);
    }

    /**
     * Lay all type
     * ADMIN - USER
     * List<TypeDto>
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllType() {
        return new ResponseEntity<>(new ApiResponse("Success", typeService.getAllType()), HttpStatus.OK);
    }

    /**
     * Lay tat ca song cua type
     * ADMIN - USER
     * List<SongDto>
     */
    @GetMapping("/{id}/songs")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getSongFromType(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Success", typeService.getSongFromType(id)), HttpStatus.OK);
    }



}