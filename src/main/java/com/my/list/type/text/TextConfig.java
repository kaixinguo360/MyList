package com.my.list.type.text;

import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import com.my.list.type.MyStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TextConfig {

    public static final String TYPE_NAME = "text";

    @Bean("TextType")
    public Type config(
        TypeConfig typeConfig,
        TextMapper textMapper
    ) {
        Type type = new Type(TYPE_NAME);

        type.setHasExtraData(true);
        type.setExtraDataClass(Text.class);
        type.setExtraDataMapper(textMapper);
        type.setNodeNormalizer(node -> node.getMainData().setCollection(false));
        type.setExcerptGenerator(node -> MyStringUtils.limit(node.getExtraData(Text.class).getContent(), 100));

        typeConfig.addType(type);
        return type;
    }
    
}
