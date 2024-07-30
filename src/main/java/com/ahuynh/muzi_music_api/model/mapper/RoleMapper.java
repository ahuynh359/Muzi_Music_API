package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.RoleDto;
import com.ahuynh.muzi_music_api.model.dto.UserDto;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.entity.role.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper extends BaseMapper<Role, RoleDto> {

    @Override
    public Role convertToEntity(RoleDto dto, Object... args) {
        Role entity = new Role();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    @Override
    public RoleDto convertToDto(Role entity, Object... args) {
        RoleDto dto = new RoleDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}