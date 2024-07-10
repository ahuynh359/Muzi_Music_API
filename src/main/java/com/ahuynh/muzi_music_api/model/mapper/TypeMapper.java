package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.TypeDto;
import com.ahuynh.muzi_music_api.model.entity.Type;
import com.ahuynh.muzi_music_api.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class TypeMapper extends BaseMapper<Type, TypeDto> {

    @Override
    public Type convertToEntity(TypeDto dto, Object... args) {
        Type entity = new Type();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    @Override
    public TypeDto convertToDto(Type entity, Object... args) {
        TypeDto dto = new TypeDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto, "createdAt", "updatedAt");
            dto.setCreatedAt(Utils.convertInstantToTime(entity.getCreatedAt()));
            dto.setUpdatedAt(Utils.convertInstantToTime(entity.getUpdatedAt()));
        }
        return dto;
    }
}