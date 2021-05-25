package com.my.list.type.text;

import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import com.my.list.util.MyStringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TextConfig {

    public static final String TYPE_NAME = "text";

    @Bean("TextType")
    public TypeDefinition config(
        TypeManager typeManager,
        TextMapper textMapper
    ) {
        TypeDefinition typeDefinition = new TypeDefinition(TYPE_NAME);

        typeDefinition.setHasExtraData(true);
        typeDefinition.setExtraDataClass(Text.class);
        typeDefinition.setExtraDataMapper(textMapper);
        typeDefinition.setNodeNormalizer(node -> node.getMainData().setCollection(false));
        typeDefinition.setExcerptGenerator(node -> MyStringUtil.limit(node.getExtraData(Text.class).getContent(), 100));

        typeManager.addType(typeDefinition);
        return typeDefinition;
    }
    
}
