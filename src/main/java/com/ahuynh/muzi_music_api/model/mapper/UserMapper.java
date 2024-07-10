package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.UserDto;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends BaseMapper<User, UserDto> {
    private RoleMapper roleMapper = new RoleMapper();

    @Override
    public User convertToEntity(UserDto dto, Object... args) {
        User entity = new User();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity, "role","createdAt","updatedAt");
            entity.setRole(roleMapper.convertToEntity(dto.getRole()));
        }
        return entity;
    }

    @Override
    public UserDto convertToDto(User entity, Object... args) {
        UserDto dto = new UserDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto, "role","createdAt","updatedAt");
            dto.setRole(roleMapper.convertToDto(entity.getRole()));
            dto.setCreatedAt(Utils.convertInstantToTime(entity.getCreatedAt()));
            dto.setUpdatedAt(Utils.convertInstantToTime(entity.getUpdatedAt()));
        }
        return dto;
    }
}