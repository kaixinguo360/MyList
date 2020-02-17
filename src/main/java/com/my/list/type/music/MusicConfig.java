package com.my.list.type.music;

import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MusicConfig {

    public static final String TYPE_NAME = "music";

    @Bean("MusicType")
    public Type config(
        TypeConfig typeConfig,
        MusicMapper musicMapper
    ) {
        Type type = new Type(TYPE_NAME);

        type.setHasExtraData(true);
        type.setExtraDataClass(Music.class);
        type.setExtraDataMapper(musicMapper);
        type.setNodeNormalizer(node -> node.getMainData().setCollection(false));
        type.setExcerptGenerator(node -> node.getExtraData(Music.class).getUrl());

        typeConfig.addType(type);
        return type;
    }
    
}
