package com.my.list.type;

import com.my.list.service.DataException;

import java.util.Map;

public interface ExtraData {
    Long getExtraId();
    void setExtraId(Long id);
    Long getParentId();
    void setParentId(Long id);
    Map<String, Object> toMap();
    void fromMap(Map<String, Object> map);
    
    static <T extends ExtraData> T parse(Class<T> clazz, Map<String, Object> map) {
        T extraValues;
        try {
            extraValues = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new DataException("Extra values class don't have a default constructor, extraValuesClass=" + clazz);
        }
        extraValues.fromMap(map);
        return extraValues;
    }
}
