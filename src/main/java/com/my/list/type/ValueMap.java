package com.my.list.type;

import java.util.Map;

public interface ValueMap {
    Map<String, Object> toMap();
    void fromMap(Map<String, Object> map);
}
