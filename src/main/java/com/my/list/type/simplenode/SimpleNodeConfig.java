package com.my.list.type.simplenode;

import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import com.my.list.util.MyStringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class SimpleNodeConfig {

    public static final String TYPE_NAME = "node";

    @Bean("SimpleNodeType")
    public TypeDefinition config(TypeManager typeManager) {
        TypeDefinition typeDefinition = new TypeDefinition(TYPE_NAME);
        
        typeDefinition.setExcerptGenerator(node -> {
            String text = node.getMainData().getTitle();
            if (StringUtils.isEmpty(text)) text = node.getMainData().getDescription();
            return MyStringUtil.limit(text, 36);
        });
        
        typeManager.addType(typeDefinition);
        return typeDefinition;
    }
    
}
