package com.my.list.type;

import java.util.Map;

public interface ExtraValues {
    Long getExtraId();
    void setExtraId(Long id);
    Long getParentId();
    void setParentId(Long id);
    Map<String, Object> toMap();
    void fromMap(Map<String, Object> map);
}
