package com.my.list.type.simplenode;

import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import com.my.list.type.MyStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class SimpleNodeConfig {

    public static final String TYPE_NAME = "node";

    @Bean("SimpleNodeType")
    public Type config(TypeConfig typeConfig) {
        Type type = new Type(TYPE_NAME);
        
        type.setExcerptGenerator(node -> {
            String text = node.getMainData().getTitle();
            if (StringUtils.isEmpty(text)) text = node.getMainData().getDescription();
            return MyStringUtils.limit(text, 36);
        });
        
        typeConfig.addType(type);
        return type;
    }
    
}
