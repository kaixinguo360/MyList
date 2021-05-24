package com.my.list.type.dlist;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.my.list.domain.ExtraData;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonTypeName(value = "dlist")
public class DList implements ExtraData {

    private Long id = null;
    private String filter = null;
    private String config = null;

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
        map.put("dlist_id", id);
        map.put("dlist_filter", filter);
        map.put("dlist_config", config);
        return map;
    }

    @Override
    public String toString() {
        return "DList{" +
            "id=" + id +
            ", filter=" + filter +
            ", config=" + config +
            '}';
    }
}
