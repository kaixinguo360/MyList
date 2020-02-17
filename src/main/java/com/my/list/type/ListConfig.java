package com.my.list.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.domain.MainData;
import com.my.list.dto.ListItem;
import com.my.list.dto.Node;
import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;

@Configuration
public class ListConfig {

    public static final String TYPE_NAME = "list";
    private final ObjectMapper objectMapper;

    public ListConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean("ListType")
    public Type config(TypeConfig typeConfig) {
        Type type = new Type(TYPE_NAME);

        type.setHasExtraList(true);
        type.setExtraListUnique(true);
        type.setExtraListRequired(true);
        type.setNodeNormalizer(node -> node.getMainData().setCollection(true));
        type.setExcerptGenerator(this::generateExcerpt);
        
        typeConfig.addType(type);
        return type;
    }

    private String generateExcerpt(Node node) {
        List<ListItem> list = node.getExtraList();
        if (list.size() == 0) {
            String text = node.getMainData().getTitle();
            if (StringUtils.isEmpty(text)) text = node.getMainData().getDescription();
            text = MyStringUtils.limit(text, 36);
            return "{\"type\":\"node\",\"excerpt\":\"" + text + "\",\"count\":0}";
        } else {
            MainData first = list.get(0).getNode().getMainData();
            return "{\"type\":\""
                + first.getType() +
                "\",\"excerpt\":\""
                + first.getExcerpt() +
                "\",\"count\":"
                + list.size() +
                "}";
        }
    }
    
}
