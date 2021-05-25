package com.my.list.type.tag;

import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import com.my.list.util.MyStringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class TagConfig {

    public static final String TYPE_NAME = "tag";

    @Bean("TagType")
    public TypeDefinition config(TypeManager typeManager) {
        TypeDefinition typeDefinition = new TypeDefinition(TYPE_NAME);
        
        typeDefinition.setHasExtraList(true);
        typeDefinition.setExtraListUnique(true);
        typeDefinition.setExtraListRequired(false);
        typeDefinition.setNodeNormalizer(node -> node.getMainData().setCollection(true));
        typeDefinition.setExcerptGenerator(node -> {
            String text = node.getMainData().getDescription();
            if (StringUtils.isEmpty(text)) text = node.getMainData().getTitle();
            return MyStringUtil.limit(text, 100);
        });
        
        typeManager.addType(typeDefinition);
        return typeDefinition;
    }
    
}
