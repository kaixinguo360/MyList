package com.my.list.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.domain.ExtraData;
import com.my.list.domain.MainData;
import com.my.list.exception.DataException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TypeConfig {

    private Map<String, Type> typeNames = new HashMap<>();
    private Map<Class<? extends ExtraData>, Type> typeClasses = new HashMap<>();
    private ObjectMapper objectMapper;

    public TypeConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void addType(Type type) {
        typeNames.put(type.getTypeName(), type);
        typeClasses.put(type.getExtraDataClass(), type);
        objectMapper.registerSubtypes(getTypes().stream()
            .filter(Type::getHasExtraData)
            .map(Type::getExtraDataClass)
            .collect(Collectors.toList())
        );
    }

    public Type getType(String typeName) {
        Type type = typeNames.get(typeName);
        if (type == null) throw new DataException("No such type, typeName=" + typeName);
        return type;
    }
    public Type getType(Class<? extends ExtraData> typeClass) {
        Type type = typeClasses.get(typeClass);
        if (type == null) throw new DataException("No such type, typeClass=" + typeClass);
        return type;
    }
    public Collection<Type> getTypes() {
        return typeNames.values();
    }

    public Type getType(MainData mainData) {
        if (mainData == null) throw new DataException("Input mainData is null.");
        return getType(mainData.getType());
    }
    public Type getType(ExtraData extraData) {
        if (extraData == null) throw new DataException("Input extraData is null.");
        return getType(extraData.getClass());
    }
}
