package com.my.list.type.image;

import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfig {

    public static final String TYPE_NAME = "image";

    @Bean("ImageType")
    public TypeDefinition config(
        TypeManager typeManager,
        ImageMapper imageMapper
    ) {
        TypeDefinition typeDefinition = new TypeDefinition(TYPE_NAME);
        
        typeDefinition.setHasExtraData(true);
        typeDefinition.setExtraDataClass(Image.class);
        typeDefinition.setExtraDataMapper(imageMapper);
        typeDefinition.setNodeNormalizer(node -> node.getMainData().setCollection(false));
        typeDefinition.setExcerptGenerator(node -> node.getExtraData(Image.class).getUrl());

        typeManager.addType(typeDefinition);
        return typeDefinition;
    }
    
}
