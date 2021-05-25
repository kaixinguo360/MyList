package com.my.list.type.list;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.entity.ListItem;
import com.my.list.entity.MainData;
import com.my.list.entity.Node;
import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import com.my.list.util.MyStringUtil;
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
    public TypeDefinition config(TypeManager typeManager) {
        TypeDefinition typeDefinition = new TypeDefinition(TYPE_NAME);

        typeDefinition.setHasExtraList(true);
        typeDefinition.setExtraListUnique(true);
        typeDefinition.setExtraListRequired(true);
        typeDefinition.setNodeNormalizer(node -> node.getMainData().setCollection(true));
        typeDefinition.setExcerptGenerator(this::generateExcerpt);
        
        typeManager.addType(typeDefinition);
        return typeDefinition;
    }

    private String generateExcerpt(Node node) {
        List<ListItem> list = node.getExtraList();
        Map<String, Object> excerpt = new HashMap<>();
        if (list.size() == 0) {
            String text = node.getMainData().getTitle();
            if (StringUtils.isEmpty(text)) text = node.getMainData().getDescription();
            text = MyStringUtil.limit(text, 36);
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
