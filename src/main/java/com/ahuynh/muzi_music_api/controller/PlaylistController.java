package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.config.security.CurrentUser;
import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.payload.request.AddPlaylistRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.service.CommentService;
import com.ahuynh.muzi_music_api.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;

    /**
     * Tao playlist
     * USER
     * PlaylistDto
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createPlaylist( @RequestBody AddPlaylistRequest request,@CurrentUser CustomUserDetail currentUser) {

        return new ResponseEntity<>(new ApiResponse("Success",
                playlistService.createPlaylist(request,currentUser)), HttpStatus.CREATED);
    }


    /**
     * Get All Playlist
     * USER
     * List<PlaylistDto>
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllPlaylist(@CurrentUser CustomUserDetail currentUser) {

        return new ResponseEntity<>(new ApiResponse("Success",
                playlistService.getAllPlaylist(currentUser)), HttpStatus.CREATED);
    }
}
