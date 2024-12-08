package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.AlbumDto;
import com.ahuynh.muzi_music_api.model.dto.SongDto;
import com.ahuynh.muzi_music_api.model.entity.Album;
import com.ahuynh.muzi_music_api.model.entity.Song;
import com.ahuynh.muzi_music_api.model.mapper.AlbumMapper;
import com.ahuynh.muzi_music_api.model.mapper.SongMapper;
import com.ahuynh.muzi_music_api.payload.request.UpdateAlbumRequest;
import com.ahuynh.muzi_music_api.repository.AlbumRepository;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import com.ahuynh.muzi_music_api.utils.SortName;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final FirebaseService firebaseService;
    private final SongMapper songMapper;
    private final SongRepository songRepository;

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


    public List<AlbumDto> getAllAlbums(SortName sort, Pageable pageable) {
        List<Album> albums;
        switch (sort) {
            case A_Z -> albums = albumRepository.findAllByOrderByNameAsc(pageable).getContent();
            case Z_A -> albums = albumRepository.findAllByOrderByNameDesc(pageable).getContent();
            case NEW -> albums = albumRepository.findAllByOrderByCreatedAtDesc(pageable).getContent();
            case OLD -> albums = albumRepository.findAllByOrderByCreatedAtAsc(pageable).getContent();
            default -> albums = albumRepository.findAllByOrderByCreatedAtDesc(pageable).getContent();
        }
        return albumMapper.convertToDtoList(albums);
    }


    public Page<SongDto> getSongsFromAlbum(Long albumId, Pageable pageable) {
        albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album not exists with id " + albumId));
        Page<Song> songsPage = songRepository.findByAlbumId(albumId, pageable);
        return songsPage.map(songMapper::convertToDto);
    }


    public AlbumDto updateAlbum(UpdateAlbumRequest request) {
        Album album = albumRepository.findById(request.getId()).orElseThrow(() ->
                new EntityNotFoundException("Album with id " + request.getId() + " not found"));
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

    public AlbumDto getAlbumById(Long id) {
        return albumMapper.convertToDto(albumRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Album with id " + id + " not found")));
    }
}