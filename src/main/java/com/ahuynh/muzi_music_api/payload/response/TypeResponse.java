package com.ahuynh.muzi_music_api.payload.response;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Type;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TypeResponse {
    private Long id;
    private String name;

    public static TypeResponse toResponse(Type type) {
        return new TypeResponse(type.getId(), type.getName());

    }

    public static List<TypeResponse> toResponseList(List<Type> types) {
        return types.stream()
                .map(TypeResponse::toResponse)
                .collect(Collectors.toList());


    }
}
