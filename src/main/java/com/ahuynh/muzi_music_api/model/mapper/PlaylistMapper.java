package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.PlaylistDto;
import com.ahuynh.muzi_music_api.model.entity.Playlist;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PlaylistMapper extends BaseMapper<Playlist, PlaylistDto> {

    @Override
    public Playlist convertToEntity(PlaylistDto dto, Object... args) {
        Playlist entity = new Playlist();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    @Override
    public PlaylistDto convertToDto(Playlist entity, Object... args) {
        PlaylistDto dto = new PlaylistDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}