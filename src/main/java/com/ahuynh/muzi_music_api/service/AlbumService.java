package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.ResourceNotFoundException;
import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import com.ahuynh.muzi_music_api.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ModelMapper modelMapper;
    private final FirebaseService firebaseService;



    public Album getAlbum(Long id) {
        return albumRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Album with id " + id + " not found"));
    }

    public void deleteAlbum(Long id) {
      albumRepository.findAlbumById(id).orElseThrow(() -> new ResourceNotFoundException("Album not exits id =" + id.toString()));
        albumRepository.deleteById(id);
    }

    public Album updateAlbum(Long iid, AlbumRequest newAlbum) {
        Album updatedAlbum = albumRepository.findAlbumById(iid).orElseThrow(() -> new ResourceNotFoundException("Album not exits id =" + iid.toString()));
        if(albumRepository.existsByName(newAlbum.getName())){
            throw new DuplicateException("Album with name " + newAlbum.getName() + " already exists");
        }

        if(newAlbum.getName() != null){
            updatedAlbum.setName(newAlbum.getName());
        }
        if(newAlbum.getDescription() != null){
            updatedAlbum.setDescription(newAlbum.getDescription());
        }

        return albumRepository.save(updatedAlbum);
    }


    public Album save(MultipartFile avatar, String name, String description) {
        if(albumRepository.existsByName(name)){
            throw new DuplicateException("Album with name " + name + " already exists");
        }
        String url = firebaseService.upload(avatar,"image/png");
        Album album = new Album(name,description,url);

        return albumRepository.save(album);
    }

}