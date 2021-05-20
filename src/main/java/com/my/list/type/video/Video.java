package com.my.list.type.video;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.my.list.domain.ExtraData;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonTypeName(value = "video")
public class Video implements ExtraData {

    private Long id = null;
    private String url = null;
    private String format = null;

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
        map.put("music_id", id);
        map.put("music_url", url);
        map.put("music_format", format);
        return map;
    }

    @Override
    public String toString() {
        return "Video{" +
            "id=" + id +
            ", url='" + url + '\'' +
            ", format='" + format + '\'' +
            '}';
    }
}
