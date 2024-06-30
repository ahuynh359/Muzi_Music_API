package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.AlbumDto;
import com.ahuynh.muzi_music_api.model.dto.SongDto;
import com.ahuynh.muzi_music_api.model.entity.Album;
import com.ahuynh.muzi_music_api.model.mapper.AlbumMapper;
import com.ahuynh.muzi_music_api.model.mapper.SongMapper;
import com.ahuynh.muzi_music_api.payload.request.UpdateAlbumRequest;
import com.ahuynh.muzi_music_api.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final FirebaseService firebaseService;
    private final SongMapper songMapper;

    public AlbumDto createAlbum(String name, MultipartFile avatar) {
        if (albumRepository.existsByName(name)) {
            throw new DuplicateException("Album with name " + name + " already exists");
        }
        String url = "";
        try {
             url = firebaseService.upload(avatar, "image/png");
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload avatar: " + e.getMessage());

        }
        return albumMapper.convertToDto(albumRepository.save(new Album(name, url)));
    }

    public void deleteAlbum(Long id) {
        Album album = albumRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Album with id " + id + " not found"));
        albumRepository.delete(album);
    }


    public List<AlbumDto> getAllAlbum() {
        return albumMapper.convertToDtoList(albumRepository.findAll());
    }


    public List<SongDto> getSongsFromAlbum(Long id) {
        albumRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Album not exits id " + id));
        return songMapper.convertToDtoList(albumRepository.findSongById(id));
    }

    public AlbumDto updateAlbum(UpdateAlbumRequest request) {
        Album album = albumRepository.findById(request.getAlbumId()).orElseThrow(() ->
                new EntityNotFoundException("Album with id " + request.getAlbumId() + " not found"));
        album.setName(request.getName());
        return albumMapper.convertToDto(albumRepository.save(album));
    }


    public AlbumDto updateAvatar(Long id, MultipartFile avatar) {
        Album album = albumRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Album with id " + id + " not found"));
        String url = firebaseService.upload(avatar, "image/png");
        album.setAvatar(url);
        return albumMapper.convertToDto(albumRepository.save(album));
    }
}