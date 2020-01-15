package com.my.list;

import com.my.list.service.Type;
import com.my.list.service.TypeConfig;
import com.my.list.type.image.Image;
import com.my.list.type.image.ImageMapper;
import com.my.list.type.music.Music;
import com.my.list.type.music.MusicMapper;
import com.my.list.type.text.Text;
import com.my.list.type.text.TextMapper;
import com.my.list.type.video.Video;
import com.my.list.type.video.VideoMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class MyListApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyListApplication.class, args);
    }
    
    @Bean
    public TypeConfig typeConfig(
        TextMapper textMapper,
        ImageMapper imageMapper,
        MusicMapper musicMapper,
        VideoMapper videoMapper
    ) {
        TypeConfig typeConfig = new TypeConfig();
        typeConfig.addType(new Type("node"));
        typeConfig.addType(new Type("list", true));
        typeConfig.addType(new Type(Text.TYPE_NAME, Text.class, textMapper));
        typeConfig.addType(new Type(Image.TYPE_NAME, Image.class, imageMapper));
        typeConfig.addType(new Type(Music.TYPE_NAME, Music.class, musicMapper));
        typeConfig.addType(new Type(Video.TYPE_NAME, Video.class, videoMapper));
        return typeConfig;
    }

}
