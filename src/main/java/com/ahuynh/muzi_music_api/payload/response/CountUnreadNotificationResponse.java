package com.ahuynh.muzi_music_api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountUnreadNotificationResponse {
    private int count;
}
