package com.my.list;

import com.my.list.domain.NodeMapper;
import com.my.list.service.ExtraNodeService;
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
    public ExtraNodeService extraNodeService(
        NodeMapper nodeMapper,
        TextMapper textMapper,
        ImageMapper imageMapper,
        MusicMapper musicMapper,
        VideoMapper videoMapper
    ) {
        ExtraNodeService.Config config = new ExtraNodeService.Config();
        config.addHandler(new ExtraNodeService.Handler("node"));
        config.addHandler(new ExtraNodeService.Handler(Text.TYPE_NAME, Text.class, textMapper));
        config.addHandler(new ExtraNodeService.Handler(Image.TYPE_NAME, Image.class, imageMapper));
        config.addHandler(new ExtraNodeService.Handler(Music.TYPE_NAME, Music.class, musicMapper));
        config.addHandler(new ExtraNodeService.Handler(Video.TYPE_NAME, Video.class, videoMapper));
        return new ExtraNodeService(config, nodeMapper);
    }

}
