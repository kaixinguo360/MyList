package com.my.list.type.tag;

import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import com.my.list.type.MyStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class TagConfig {

    public static final String TYPE_NAME = "tag";

    @Bean("TagType")
    public Type config(TypeConfig typeConfig) {
        Type type = new Type(TYPE_NAME);
        
        type.setHasExtraList(true);
        type.setExtraListUnique(true);
        type.setExtraListRequired(false);
        type.setNodeNormalizer(node -> node.getMainData().setCollection(true));
        type.setExcerptGenerator(node -> {
            String text = node.getMainData().getDescription();
            if (StringUtils.isEmpty(text)) text = node.getMainData().getTitle();
            return MyStringUtils.limit(text, 100);
        });
        
        typeConfig.addType(type);
        return type;
    }
    
}
