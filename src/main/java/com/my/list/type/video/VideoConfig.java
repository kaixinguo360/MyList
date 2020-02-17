package com.my.list.type.video;

import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoConfig {

    public static final String TYPE_NAME = "video";

    @Bean("VideoType")
    public Type config(
        TypeConfig typeConfig,
        VideoMapper videoMapper
    ) {
        Type type = new Type(TYPE_NAME);

        type.setHasExtraData(true);
        type.setExtraDataClass(Video.class);
        type.setExtraDataMapper(videoMapper);
        type.setNodeNormalizer(node -> node.getMainData().setCollection(false));
        type.setExcerptGenerator(node -> node.getExtraData(Video.class).getUrl());

        typeConfig.addType(type);
        return type;
    }
    
}
