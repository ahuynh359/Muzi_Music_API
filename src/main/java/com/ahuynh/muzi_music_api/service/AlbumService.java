package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.ResourceNotFoundException;
import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import com.ahuynh.muzi_music_api.repository.AlbumRepository;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ModelMapper modelMapper;
    private final FirebaseService firebaseService;
    private final SongRepository songRepository;


    public Album getAlbumById(Long id) {
        return albumRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Album with id " + id + " not found"));
    }

    public List<Album> getAlbum() {
        if (albumRepository.count() == 0) {
            throw new ResourceNotFoundException("There is no album");
        }
        return albumRepository.findAll();
    }

    public List<Song> getSongFromAlbum(Long id) {
        if (albumRepository.count() == 0) {
            throw new ResourceNotFoundException("There is no album");
        }
        albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Album not exits id =" + id.toString()));
        return albumRepository.findSongById(id);
    }

    public void deleteAlbum(Long id) {
        albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Album not exits id =" + id.toString()));
        albumRepository.deleteById(id);
    }

    public Album updateAlbum(Long iid, AlbumRequest newAlbum) {
        Album updatedAlbum = albumRepository.findById(iid).orElseThrow(() -> new ResourceNotFoundException("Album not exits id =" + iid.toString()));
        if (albumRepository.existsByName(newAlbum.getName())) {
            throw new DuplicateException("Album with name " + newAlbum.getName() + " already exists");
        }

        if (newAlbum.getName() != null) {
            updatedAlbum.setName(newAlbum.getName());
        }
        if (newAlbum.getDescription() != null) {
            updatedAlbum.setDescription(newAlbum.getDescription());
        }

        return albumRepository.save(updatedAlbum);
    }


    public Album save(MultipartFile avatar, String name, String description) {
        if (albumRepository.existsByName(name)) {
            throw new DuplicateException("Album with name " + name + " already exists");
        }
        String url = firebaseService.upload(avatar, "image/png");
        Album album = new Album(name, description, url);

        return albumRepository.save(album);
    }

    public Album getAlbumByName(String name) {
        return albumRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Album with name " + name + " not found"));
    }

    public void addSongToAlbum(Long songId, Long albumId) {
        Album album = albumRepository.findById(albumId).orElseThrow(() ->
                new ResourceNotFoundException("Album with id " + albumId + " not found"));
        Song song = songRepository.findById(songId).orElseThrow(() ->
                new ResourceNotFoundException("Song with id " + songId + " not found"));
        album.addSong(song);
        albumRepository.save(album);
    }

    public void deleteSongFromAlbum(Long songId, Long albumId) {
        Album album = albumRepository.findById(albumId).orElseThrow(() ->
                new ResourceNotFoundException("Album with id " + albumId + " not found"));
        Song song = songRepository.findById(songId).orElseThrow(() ->
                new ResourceNotFoundException("Song with id " + songId + " not found"));
        album.removeSong(song);
        albumRepository.save(album);
    }
}