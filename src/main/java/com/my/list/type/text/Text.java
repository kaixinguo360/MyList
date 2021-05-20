package com.my.list.type.text;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.my.list.domain.ExtraData;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonTypeName(value = "text")
public class Text implements ExtraData {

    private Long id = null;
    private String content = null;

    @Override
    public Long getExtraId() {
        return id;
    }

    @Override
    public void setExtraId(Long id) {
        this.id = id;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("text_id", id);
        map.put("text_content", content);
        return map;
    }

    @Override
    public String toString() {
        return "Text{" +
            "id=" + id +
            ", content='" + content + '\'' +
            '}';
    }
}
