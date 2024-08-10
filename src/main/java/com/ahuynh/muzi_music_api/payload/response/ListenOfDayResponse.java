package com.ahuynh.muzi_music_api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListenOfDayResponse {
    private String day;
    private int listen;
}
