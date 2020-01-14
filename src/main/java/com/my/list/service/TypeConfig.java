package com.my.list.service;

import java.util.HashMap;
import java.util.Map;

public class TypeConfig {
    
    private Map<String, Type> handlers = new HashMap<>();

    public void addType(Type type) {
        handlers.put(type.typeName, type);
    }

    Type getType(String typeName) {
        Type type = handlers.get(typeName);
        if (type == null) throw new DataException("No such handler, typeName=" + typeName);
        return type;
    }
}
