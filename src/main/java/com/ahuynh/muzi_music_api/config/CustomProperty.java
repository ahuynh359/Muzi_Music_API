package com.ahuynh.muzi_music_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "com.ahuynh")
public class CustomProperty {

    String jwtSecret;
    long jwtExp;

}
