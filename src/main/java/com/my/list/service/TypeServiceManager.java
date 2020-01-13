package com.my.list.service;

import java.util.HashMap;
import java.util.Map;

public class TypeServiceManager {
    
    Map<String, TypeService> services = new HashMap<>();

    public void putService(String type, TypeService typeService) {
        services.put(type, typeService);
    }

    public TypeService getService(String type) {
        TypeService handler = services.get(type);
        if (handler == null) throw new RuntimeException("No such type: " + type);
        return handler;
    }
}
