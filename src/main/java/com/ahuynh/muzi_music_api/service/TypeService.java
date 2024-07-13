package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.AlbumDto;
import com.ahuynh.muzi_music_api.model.dto.SongDto;
import com.ahuynh.muzi_music_api.model.dto.TypeDto;
import com.ahuynh.muzi_music_api.model.entity.Album;
import com.ahuynh.muzi_music_api.model.entity.Type;
import com.ahuynh.muzi_music_api.model.mapper.SongMapper;
import com.ahuynh.muzi_music_api.model.mapper.TypeMapper;
import com.ahuynh.muzi_music_api.payload.request.UpdateTypeRequest;
import com.ahuynh.muzi_music_api.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeService {
    private final TypeRepository typeRepository;
    private final TypeMapper typeMapper;
    private final SongMapper songMapper;
    private final FirebaseService firebaseService;

    public TypeDto createType(String name, MultipartFile avatar) {
        if (typeRepository.existsByName(name)) {
            throw new DuplicateException("Type with name " + name + " already exists");
        }
        String url = firebaseService.upload(avatar, "image/png");
        return typeMapper.convertToDto(typeRepository.save(new Type(name, url)));
    }

    public TypeDto getTypeById(Long id) {
        return typeMapper.convertToDto(typeRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Type with id " + id + " not found")));
    }

    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    public TypeDto updateType(UpdateTypeRequest request) {
        Type updateType = typeRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Type not found with id: " + request.getId()));

        if (request.getName() != null && !request.getName().isEmpty()) {
            if (typeRepository.existsByName(request.getName()) && !updateType.getName().equals(request.getName())) {
                throw new DuplicateException("Type with name " + request.getName() + " already exists");
            }
            updateType.setName(request.getName());
        }


        return typeMapper.convertToDto(typeRepository.save(updateType));
    }

    public List<TypeDto> getAllType() {
        return typeMapper.convertToDtoList(typeRepository.findAll());
    }

    public List<SongDto> getSongFromType(Long id) {

        return songMapper.convertToDtoList(typeRepository.findSongById(id));

    }


    public TypeDto updateAvatar(Long id, MultipartFile avatar) {
        Type type = typeRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Type with id " + id + " not found"));
        String url = firebaseService.upload(avatar, "image/png");
        type.setAvatar(url);
        return typeMapper.convertToDto(typeRepository.save(type));
    }
}
