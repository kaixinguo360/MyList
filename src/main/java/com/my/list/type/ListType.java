package com.my.list.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.SpringUtil;
import com.my.list.dto.ListItem;
import com.my.list.dto.Node;
import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ListType implements Type {

    private static final String TYPE_NAME = "list";
    private final ObjectMapper objectMapper;

    public ListType() {
        this.objectMapper = new ObjectMapper();
    }

    public String getTypeName() {
        return TYPE_NAME;
    }
    public boolean isHasExtraList() {
        return true;
    }

    public void process(Node node) {
        List<ListItem> list = node.getExtraList();
        if (list.size() != 0) {
            try {
                Node firstNode = node.getExtraList().get(0).node;

                if (firstNode.getMainData().getExcerpt() == null) {
                    Type type = SpringUtil.getBean(TypeConfig.class).getType(firstNode.getMainData());
                    type.process(firstNode);
                }

                Map<String, String> excerpt = new HashMap<>();
                excerpt.put("type", firstNode.getMainData().getType());
                excerpt.put("excerpt", firstNode.getMainData().getExcerpt());
                excerpt.put("count", "" + list.size());
                
                node.getMainData().setExcerpt(objectMapper.writeValueAsString(excerpt));
                
            } catch (Exception e) {
                node.getMainData().setExcerpt("{\"type\":\"node\",\"excerpt\":\"Unknown\",\"count\":0}");
            }
        } else {
            node.getMainData().setExcerpt("{\"type\":\"node\",\"excerpt\":\"No Content\",\"count\":0}");
        }
    }
}
