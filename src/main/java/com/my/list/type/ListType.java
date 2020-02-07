package com.my.list.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.dto.Type;
import org.springframework.stereotype.Component;

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
}
