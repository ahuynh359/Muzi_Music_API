package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.CommentDto;
import com.ahuynh.muzi_music_api.model.entity.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class CommentMapper extends BaseMapper<Comment, CommentDto> {
    private UserMapper userMapper = new UserMapper();

    @Override
    public Comment convertToEntity(CommentDto dto, Object... args) {
        Comment entity = new Comment();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity, "users", "time");
            entity.setUser(userMapper.convertToEntity(dto.getUser()));
        }
        return entity;
    }

    @Override
    public CommentDto convertToDto(Comment entity, Object... args) {
        CommentDto dto = new CommentDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto, "users", "time");
            dto.setUser(userMapper.convertToDto(entity.getUser()));
            dto.setTime(calTime(entity.getUpdatedAt()));
        }
        return dto;
    }

    private String calTime(Instant updateAt) {
        Duration duration = Duration.between(updateAt, Instant.now());
        long second = duration.toSeconds();
        long minute = duration.toMinutes();
        long hour = duration.toHours();
        if (hour == 0) {
            if (minute == 0 && second < 59) {
                return "Now";
            } else if (minute < 59) {
                return minute + " minutes ago";
            }
        }

        ZonedDateTime zonedDateTime = updateAt.atZone(ZoneId.of("UTC"));

        // Extract year, month, and day
        int year = zonedDateTime.getYear();
        int month = zonedDateTime.getMonthValue();
        int day = zonedDateTime.getDayOfMonth();
        return day + "/" + month + "/" + year;

    }
}