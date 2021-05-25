package com.my.list.type.dlist;

import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import com.my.list.util.MyStringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class DListConfig {

    public static final String TYPE_NAME = "dlist";

    @Bean("DListType")
    public TypeDefinition config(
        TypeManager typeManager,
        DListMapper DListMapper
    ) {
        TypeDefinition typeDefinition = new TypeDefinition(TYPE_NAME);
        
        typeDefinition.setHasExtraData(true);
        typeDefinition.setExtraDataClass(DList.class);
        typeDefinition.setExtraDataMapper(DListMapper);
        typeDefinition.setNodeNormalizer(node -> node.getMainData().setCollection(false));
        typeDefinition.setExcerptGenerator(node -> {
            String text = node.getMainData().getDescription();
            if (StringUtils.isEmpty(text)) text = node.getMainData().getTitle();
            return MyStringUtil.limit(text, 100);
        });

        typeManager.addType(typeDefinition);
        return typeDefinition;
    }
    
}
