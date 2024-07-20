package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.SingerDto;
import com.ahuynh.muzi_music_api.model.entity.Singer;
import com.ahuynh.muzi_music_api.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class SingerMapper extends BaseMapper<Singer, SingerDto> {


    @Override
    public Singer convertToEntity(SingerDto dto, Object... args) {
        Singer entity = new Singer();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);

        }
        return entity;
    }

    @Override
    public SingerDto convertToDto(Singer entity, Object... args) {
        SingerDto dto = new SingerDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto,"createdAt","updatedAt");
            dto.setCreatedAt(Utils.convertInstantToTime(entity.getCreatedAt()));
            dto.setUpdatedAt(Utils.convertInstantToTime(entity.getUpdatedAt()));
        }
        return dto;
    }
}
