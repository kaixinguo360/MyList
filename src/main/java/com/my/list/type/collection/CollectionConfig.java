package com.my.list.type.collection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.entity.Node;
import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import com.my.list.util.MyStringUtil;
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
    public TypeDefinition config(TypeManager typeManager) {
        TypeDefinition typeDefinition = new TypeDefinition(TYPE_NAME);

        typeDefinition.setAllowCascade(true);
        typeDefinition.setHasExtraList(true);
        typeDefinition.setExtraListUnique(true);
        typeDefinition.setExtraListRequired(true);
        typeDefinition.setNodeNormalizer(node -> node.getMainData().setCollection(true));
        typeDefinition.setExcerptGenerator(this::generateExcerpt);
        
        typeManager.addType(typeDefinition);
        return typeDefinition;
    }

    private String generateExcerpt(Node node) {
        List<Node> children = node.getExtraList().stream().map(i -> i.node).collect(Collectors.toList());
        Map<String, Object> excerpt = new HashMap<>();
        List<Map<String, Object>> excerpts = new ArrayList<>();
        if (children.size() == 0) {
            String text = node.getMainData().getTitle();
            if (StringUtils.isEmpty(text)) text = node.getMainData().getDescription();
            text = MyStringUtil.limit(text, 36);

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
