package com.my.list.service;

import java.util.HashMap;
import java.util.Map;

public class TypeConfig {
    
    private Map<String, Type> types = new HashMap<>();

    public void addType(Type type) {
        types.put(type.typeName, type);
    }

    Type getType(String typeName) {
        Type type = types.get(typeName);
        if (type == null) throw new DataException("No such type, typeName=" + typeName);
        return type;
    }
}
