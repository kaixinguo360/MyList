package com.my.list.type.image;

import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfig {

    public static final String TYPE_NAME = "image";

    @Bean("ImageType")
    public Type config(
        TypeConfig typeConfig,
        ImageMapper imageMapper
    ) {
        Type type = new Type(TYPE_NAME);
        
        type.setHasExtraData(true);
        type.setExtraDataClass(Image.class);
        type.setExtraDataMapper(imageMapper);
        type.setNodeNormalizer(node -> node.getMainData().setCollection(false));
        type.setExcerptGenerator(node -> node.getExtraData(Image.class).getUrl());

        typeConfig.addType(type);
        return type;
    }
    
}
