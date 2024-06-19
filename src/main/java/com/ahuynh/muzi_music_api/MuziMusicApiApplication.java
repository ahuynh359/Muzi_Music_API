package com.ahuynh.muzi_music_api;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EntityScan(basePackageClasses = {MuziMusicApiApplication.class, Jsr310JpaConverters.class})
public class MuziMusicApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(MuziMusicApiApplication.class, args);

	}

//	@Bean
//	public ModelMapper modelMapper(){
//		return new ModelMapper();
//	}


}
