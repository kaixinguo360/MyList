package com.my.list.type;

import com.my.list.dto.Type;
import org.springframework.stereotype.Component;

@Component
public class TagType implements Type {

    private static final String TYPE_NAME = "tag";

    public String getTypeName() {
        return TYPE_NAME;
    }
    public boolean isHasExtraList() {
        return true;
    }
    public boolean isExtraListUnique() {
        return true;
    }
    public boolean isExtraListRequired() {
        return false;
    }
}
