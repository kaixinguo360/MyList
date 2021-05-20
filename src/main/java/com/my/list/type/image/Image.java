package com.my.list.type.image;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.my.list.domain.ExtraData;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonTypeName(value = "image")
public class Image implements ExtraData {

    private Long id = null;
    private String url = null;
    private String type = null;
    private String author = null;
    private String gallery = null;
    private String source = null;

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
        map.put("image_id", id);
        map.put("image_url", url);
        map.put("image_type", type);
        map.put("image_author", author);
        map.put("image_gallery", gallery);
        map.put("image_source", source);
        return map;
    }

    @Override
    public String toString() {
        return "Image{" +
            "id=" + id +
            ", url='" + url + '\'' +
            ", type='" + type + '\'' +
            ", author='" + author + '\'' +
            ", gallery='" + gallery + '\'' +
            ", source='" + source + '\'' +
            '}';
    }
}
