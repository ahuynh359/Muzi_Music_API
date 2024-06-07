package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import com.ahuynh.muzi_music_api.payload.response.AlbumResponse;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.SongResponse;
import com.ahuynh.muzi_music_api.service.AlbumService;
import com.ahuynh.muzi_music_api.service.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    private final ObjectMapper objectMapper;

    /**
     *Thêm Album
     * ADMIN
     *AlbumResponse
     */

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addAlbum(@RequestParam("avatar") MultipartFile avatar,
                                      @RequestParam("name") String name,
                                      @RequestParam("description") String description) {

        Album albumResponse = albumService.save(avatar, name, description);
        return new ResponseEntity<>
                (new ApiResponse(true, "Create Successfully",
                        objectMapper.convertValue(albumResponse, AlbumResponse.class)), HttpStatus.CREATED);
    }

    /**
     * Lấy album theo id
     * ADMIN - USER
     *AlbumResponse
     */
    @GetMapping("/get-by-id/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAlbumById(@PathVariable Long id) {
        Album album = albumService.getAlbumById(id);
        return new ResponseEntity<>(objectMapper.convertValue(album, AlbumResponse.class), HttpStatus.OK);
    }

    /**
     * Lấy hết album
     * ADMIN - USER
     *AlbumResponse
     */
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllAlbum() {
        List<Album> album = albumService.getAlbum();
        List<AlbumResponse> responses = AlbumResponse.toResponseList(album);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", responses), HttpStatus.OK);
    }

    /**
     * Lấy album theo name
     * ADMIN - USER
     *AlbumResponse
     */
    @GetMapping("/get-by-name")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAlbumByName(@RequestParam String name) {
        Album album = albumService.getAlbumByName(name);
        return new ResponseEntity<>(objectMapper.convertValue(album, AlbumResponse.class), HttpStatus.OK);
    }

    /**
     * Lấy song từ album theo id
     * ADMIN - USER
     *AlbumResponse
     */
    @GetMapping("/get-song-from-album/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getSongFromAlbum(@PathVariable(name = "id") Long id) {
        List<Song> songs = albumService.getSongFromAlbum(id);
        List<SongResponse> responses = SongResponse.toResponseList(songs);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully",
                responses), HttpStatus.OK);
    }

    /**
     * Delete Alum
     * ADMIN
     *
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAlbum(@PathVariable(name = "id") Long id) {
        albumService.deleteAlbum(id);
        return new ResponseEntity<>(new ApiResponse(true, "Delete Successfully", ""), HttpStatus.OK);
    }


    /**
     * Update Alum
     * ADMIN
     *
     */
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateAlbum(@PathVariable(name = "id") Long id, @RequestBody AlbumRequest newAlbum) {
        Album album = albumService.updateAlbum(id, newAlbum);
        return new ResponseEntity<>(new ApiResponse(true, "Update Successfully", objectMapper.convertValue(album, AlbumResponse.class)), HttpStatus.OK);
    }

    /**
     * Thêm song to album
     * ADMIN
     *
     */
    @PostMapping("/add-song-to-album")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addSongToAlbum(@RequestParam("songId") Long songId,
                                            @RequestParam("albumId") Long albumId
    ) {
        albumService.addSongToAlbum(songId, albumId);
        return new ResponseEntity<>(new ApiResponse(true,
                "Add Successfully", ""), HttpStatus.OK);
    }
    /**
     * Delete song from album
     * ADMIN
     *
     */
    @PostMapping("/delete-song-from-album")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteSongFromAlbum(@RequestParam("songId") Long songId,
                                            @RequestParam("albumId") Long albumId
    ) {
        albumService.deleteSongFromAlbum(songId, albumId);
        return new ResponseEntity<>(new ApiResponse(true,
                "Add Successfully", ""), HttpStatus.OK);
    }


}