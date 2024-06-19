package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.UserDto;
import com.ahuynh.muzi_music_api.model.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends BaseMapper<User, UserDto> {
    private RoleMapper roleMapper = new RoleMapper();

    @Override
    public User convertToEntity(UserDto dto, Object... args) {
        User entity = new User();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity, "roles");
            entity.setRoles(roleMapper.convertToEntitySet(dto.getRoles()));
        }
        return entity;
    }

    @Override
    public UserDto convertToDto(User entity, Object... args) {
        UserDto dto = new UserDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto, "roles");
            dto.setRoles(roleMapper.convertToDtoSet(entity.getRoles()));
        }
        return dto;
    }
}