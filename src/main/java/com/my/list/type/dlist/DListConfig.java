package com.my.list.type.dlist;

import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import com.my.list.type.MyStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class DListConfig {

    public static final String TYPE_NAME = "dlist";

    @Bean("DListType")
    public Type config(
        TypeConfig typeConfig,
        DListMapper DListMapper
    ) {
        Type type = new Type(TYPE_NAME);
        
        type.setHasExtraData(true);
        type.setExtraDataClass(DList.class);
        type.setExtraDataMapper(DListMapper);
        type.setNodeNormalizer(node -> node.getMainData().setCollection(false));
        type.setExcerptGenerator(node -> {
            String text = node.getMainData().getDescription();
            if (StringUtils.isEmpty(text)) text = node.getMainData().getTitle();
            return MyStringUtils.limit(text, 100);
        });

        typeConfig.addType(type);
        return type;
    }
    
}
