package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.AlbumDto;
import com.ahuynh.muzi_music_api.model.entity.Album;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class AlbumMapper extends BaseMapper<Album, AlbumDto> {


    @Override
    public Album convertToEntity(AlbumDto dto, Object... args) {
        Album entity = new Album();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);

        }
        return entity;
    }

    @Override
    public AlbumDto convertToDto(Album entity, Object... args) {
        AlbumDto dto = new AlbumDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}