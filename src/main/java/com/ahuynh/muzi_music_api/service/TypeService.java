package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.ResourceNotFoundException;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.model.Type;
import com.ahuynh.muzi_music_api.payload.request.TypeRequest;
import com.ahuynh.muzi_music_api.repository.TypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeService {
    private final TypeRepository typeRepository;
    private final ObjectMapper objectMapper;

    public Type save(TypeRequest typeRequest) {
        if(typeRepository.existsByName(typeRequest.getName())){
            throw new DuplicateException("Type with name " + typeRequest.getName() + " already exists");
        }
        Type type = objectMapper.convertValue(typeRequest, Type.class);
        return typeRepository.save(type);
    }

    public Type getType(Long id) {
        return typeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Type with id " + id + " not found"));
    }

    public void deleteType(Long id) {
        typeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Type with id " + id + " not found"));
        typeRepository.deleteById(id);
    }

    public Type updateType(Long id, TypeRequest typeRequest) {
        Type updateType = typeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Type not exits id =" + id));


        if (typeRequest.getName() != null) {
            updateType.setName(typeRequest.getName());
        }
        if (typeRequest.getDescription() != null) {
            updateType.setDescription(typeRequest.getDescription());
        }


        return typeRepository.save(updateType);
    }

    public List<Type> getAllType() {
        return typeRepository.findAll();
    }

    public List<Song> getSongFromType(Long id) {
        if(typeRepository.count() == 0){
            throw new ResourceNotFoundException("There is no type");
        }
        typeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Type not exits id =" + id.toString()));
        return typeRepository.findSongById(id);

    }
}
