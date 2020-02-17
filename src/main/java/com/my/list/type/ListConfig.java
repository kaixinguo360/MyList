package com.my.list.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.domain.MainData;
import com.my.list.dto.ListItem;
import com.my.list.dto.Node;
import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> excerpt = new HashMap<>();
        if (list.size() == 0) {
            String text = node.getMainData().getTitle();
            if (StringUtils.isEmpty(text)) text = node.getMainData().getDescription();
            text = MyStringUtils.limit(text, 36);
            excerpt.put("type", "node");
            excerpt.put("excerpt", text);
            excerpt.put("count", 0);
        } else {
            MainData first = list.get(0).getNode().getMainData();
            excerpt.put("type", first.getType());
            excerpt.put("excerpt", first.getExcerpt());
            excerpt.put("count", list.size());
        }
        try {
            return objectMapper.writeValueAsString(excerpt);
        } catch (JsonProcessingException e) {
            return "{\"type\":\"node\",\"excerpt\":\"Error\",\"count\":0}";
        }
    }
    
}
