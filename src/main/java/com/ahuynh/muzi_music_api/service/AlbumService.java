package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.ResourceNotFoundException;
import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.role.RoleName;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import com.ahuynh.muzi_music_api.payload.response.AlbumResponse;
import com.ahuynh.muzi_music_api.repository.AlbumRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public void save(AlbumRequest albumRequest) {
        Album album = convertAlbum(albumRequest);
        albumRepository.save(album);
    }

    private Album convertAlbum(AlbumRequest albumRequest) {
        Album album = new Album();
        album.setAvatar(albumRequest.getAvatar());
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

    public ResponseEntity<AlbumResponse> updateAlbum(Long iid, AlbumRequest newAlbum) {
        Album album = albumRepository.findAlbumById(iid).orElseThrow(() -> new ResourceNotFoundException(iid.toString()));
        album.setName(newAlbum.getName());
        Album updatedAlbum = albumRepository.save(album);
        AlbumResponse albumResponse = new AlbumResponse();

        modelMapper.map(updatedAlbum, albumResponse);

        return new ResponseEntity<>(albumResponse, HttpStatus.OK);
    }


}