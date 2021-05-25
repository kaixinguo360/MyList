package com.my.list.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.entity.ExtraData;
import com.my.list.entity.MainData;
import com.my.list.exception.DataException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TypeManager {

    private final Map<String, TypeDefinition> typeNames = new HashMap<>();
    private final Map<Class<? extends ExtraData>, TypeDefinition> typeClasses = new HashMap<>();
    private final ObjectMapper objectMapper;

    public TypeManager(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void addType(TypeDefinition typeDefinition) {
        typeNames.put(typeDefinition.getTypeName(), typeDefinition);
        typeClasses.put(typeDefinition.getExtraDataClass(), typeDefinition);
        objectMapper.registerSubtypes(getAllTypes().stream()
            .filter(TypeDefinition::getHasExtraData)
            .map(TypeDefinition::getExtraDataClass)
            .collect(Collectors.toList())
        );
    }

    public Collection<TypeDefinition> getAllTypes() {
        return typeNames.values();
    }

    public TypeDefinition getType(String typeName) {
        TypeDefinition typeDefinition = typeNames.get(typeName);
        if (typeDefinition == null) throw new DataException("No such type, typeName=" + typeName);
        return typeDefinition;
    }
    public TypeDefinition getType(Class<? extends ExtraData> typeClass) {
        TypeDefinition typeDefinition = typeClasses.get(typeClass);
        if (typeDefinition == null) throw new DataException("No such type, typeClass=" + typeClass);
        return typeDefinition;
    }
    public TypeDefinition getType(MainData mainData) {
        if (mainData == null) throw new DataException("Input mainData is null.");
        return getType(mainData.getType());
    }
    public TypeDefinition getType(ExtraData extraData) {
        if (extraData == null) throw new DataException("Input extraData is null.");
        return getType(extraData.getClass());
    }
}
