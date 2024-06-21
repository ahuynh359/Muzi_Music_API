package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.CommentDto;
import com.ahuynh.muzi_music_api.model.entity.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper extends BaseMapper<Comment, CommentDto> {
    private UserMapper userMapper = new UserMapper();

    @Override
    public Comment convertToEntity(CommentDto dto, Object... args) {
        Comment entity = new Comment();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity,"users");
            entity.setUser(userMapper.convertToEntity(dto.getUser()));
        }
        return entity;
    }

    @Override
    public CommentDto convertToDto(Comment entity, Object... args) {
        CommentDto dto = new CommentDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto,"users");
            dto.setUser(userMapper.convertToDto(entity.getUser()));
        }
        return dto;
    }
}