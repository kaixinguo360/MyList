package com.my.list.type.music;

import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MusicConfig {

    public static final String TYPE_NAME = "music";

    @Bean("MusicType")
    public TypeDefinition config(
        TypeManager typeManager,
        MusicMapper musicMapper
    ) {
        TypeDefinition typeDefinition = new TypeDefinition(TYPE_NAME);

        typeDefinition.setHasExtraData(true);
        typeDefinition.setExtraDataClass(Music.class);
        typeDefinition.setExtraDataMapper(musicMapper);
        typeDefinition.setNodeNormalizer(node -> node.getMainData().setCollection(false));
        typeDefinition.setExcerptGenerator(node -> node.getExtraData(Music.class).getUrl());

        typeManager.addType(typeDefinition);
        return typeDefinition;
    }
    
}
