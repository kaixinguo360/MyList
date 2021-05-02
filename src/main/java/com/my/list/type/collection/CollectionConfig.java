package com.my.list.type.collection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.dto.Node;
import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import com.my.list.type.MyStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class CollectionConfig {

    public static final String TYPE_NAME = "collection";
    private final ObjectMapper objectMapper;

    public CollectionConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean("CollectionType")
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
        List<Node> children = node.getExtraList().stream().map(i -> i.node).collect(Collectors.toList());
        Map<String, Object> excerpt = new HashMap<>();
        List<Map<String, Object>> excerpts = new ArrayList<>();
        if (children.size() == 0) {
            String text = node.getMainData().getTitle();
            if (StringUtils.isEmpty(text)) text = node.getMainData().getDescription();
            text = MyStringUtils.limit(text, 36);

            Map<String, Object> e = new HashMap<>();
            e.put("type", "node");
            e.put("excerpt", text);
            excerpts.add(e);

            excerpt.put("excerpts", excerpts);
            excerpt.put("count", 0);
        } else {
            excerpts = children.stream().limit(6).map(n -> {
                Map<String, Object> e = new HashMap<>();
                e.put("type", n.getMainData().getType());
                e.put("excerpt", n.getMainData().getExcerpt());
                return e;
            }).collect(Collectors.toList());
            excerpt.put("excerpts", excerpts);
            excerpt.put("count", children.size());
        }
        try {
            return objectMapper.writeValueAsString(excerpt);
        } catch (JsonProcessingException e) {
            return "{\"type\":\"node\",\"excerpt\":\"Error\",\"count\":0}";
        }
    }
    
}
