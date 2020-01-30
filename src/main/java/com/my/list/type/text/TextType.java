package com.my.list.type.text;

import com.my.list.domain.ExtraData;
import com.my.list.domain.ExtraDataMapper;
import com.my.list.dto.Node;
import com.my.list.dto.Type;
import com.my.list.exception.DataException;
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
    
    public void process(Node node) {
        Text text = node.getExtraData(Text.class);
        String content = text.getContent();
        if (content == null) throw new DataException("Input extraData.content is null.");
        node.getMainData().setExcerpt(
            content.substring(0, Math.min(content.length(), 100))
        );
    }
}
