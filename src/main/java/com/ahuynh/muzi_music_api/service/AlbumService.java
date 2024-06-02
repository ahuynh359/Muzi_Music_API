package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import com.ahuynh.muzi_music_api.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private static final String UPLOAD_DIR = "images/";
    @Autowired
    private AlbumRepository albumRepository;

    private String saveImage(MultipartFile avatar) {
        if (avatar != null && !avatar.isEmpty()) {
            try {
                byte[] bytes = avatar.getBytes();
                Path path = Paths.get(UPLOAD_DIR + avatar.getOriginalFilename());
                Files.write(path, bytes);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception properly
            }
        }
        return null;
    }


    public void save(AlbumRequest albumRequest) {
//        String avatarPath = saveImage(albumRequest.getAvatar());
//        Album album = convertAlbum(albumRequest, avatarPath);
//        albumRepository.save(album);
    }

    private Album convertAlbum(AlbumRequest albumRequest, String avatarPath) {
        Album album = new Album();
        album.setAvatar(avatarPath);
        album.setName(albumRequest.getName());
        album.setDescription(albumRequest.getDescription());
        return album;
    }


    public Album getAlbum(Long id) {
        return albumRepository.findById(id).orElse(null);
    }

    public void deleteAlbum(Long id) {
        albumRepository.deleteById(id);
    }
}