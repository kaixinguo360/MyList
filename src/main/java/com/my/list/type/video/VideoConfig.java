package com.my.list.type.video;

import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoConfig {

    public static final String TYPE_NAME = "video";

    @Bean("VideoType")
    public TypeDefinition config(
        TypeManager typeManager,
        VideoMapper videoMapper
    ) {
        TypeDefinition typeDefinition = new TypeDefinition(TYPE_NAME);

        typeDefinition.setHasExtraData(true);
        typeDefinition.setExtraDataClass(Video.class);
        typeDefinition.setExtraDataMapper(videoMapper);
        typeDefinition.setNodeNormalizer(node -> node.getMainData().setCollection(false));
        typeDefinition.setExcerptGenerator(node -> node.getExtraData(Video.class).getUrl());

        typeManager.addType(typeDefinition);
        return typeDefinition;
    }
    
}
