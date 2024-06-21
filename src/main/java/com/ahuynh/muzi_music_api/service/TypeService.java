package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.SongDto;
import com.ahuynh.muzi_music_api.model.dto.TypeDto;
import com.ahuynh.muzi_music_api.model.entity.Type;
import com.ahuynh.muzi_music_api.model.mapper.SingerMapper;
import com.ahuynh.muzi_music_api.model.mapper.SongMapper;
import com.ahuynh.muzi_music_api.model.mapper.TypeMapper;
import com.ahuynh.muzi_music_api.payload.request.TypeRequest;
import com.ahuynh.muzi_music_api.payload.request.UpdateTypeRequest;
import com.ahuynh.muzi_music_api.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeService {
    private final TypeRepository typeRepository;
    private final TypeMapper typeMapper;
    private final SongMapper songMapper;

    public TypeDto createType(TypeRequest request) {
        if (typeRepository.existsByName(request.getName())) {
            throw new DuplicateException("Type with name " + request.getName() + " already exists");
        }
        return typeMapper.convertToDto(typeRepository.save(new Type(request.getName())));
    }

    public Type getTypeById(Long id) {
        return typeRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Type with id " + id + " not found"));
    }

    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    public TypeDto updateType(UpdateTypeRequest request) {
        Type updateType = typeRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("Type not exits id =" + request.getId()));

        if(typeRepository.existsByName(request.getName())) {
            throw new DuplicateException("Type with name " + request.getName() + " already exists");
        }
        if (request.getName() != null) {
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
}
