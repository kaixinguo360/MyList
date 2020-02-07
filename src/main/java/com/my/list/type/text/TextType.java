package com.my.list.type.text;

import com.my.list.domain.ExtraData;
import com.my.list.domain.ExtraDataMapper;
import com.my.list.dto.Type;
import org.springframework.stereotype.Component;

@Component
public class TextType implements Type {
    
    private final TextMapper textMapper;

    public TextType(TextMapper textMapper) {
        this.textMapper = textMapper;
    }

    public String getTypeName() {
        return Text.TYPE_NAME;
    }
    public Class<? extends ExtraData> getExtraDataClass() {
        return Text.class;
    }
    public ExtraDataMapper getExtraDataMapper() {
        return textMapper;
    }
    public boolean isHasExtraData() {
        return true;
    }
    public boolean isHasExtraList() {
        return false;
    }
}
