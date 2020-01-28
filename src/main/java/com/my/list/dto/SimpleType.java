package com.my.list.dto;

import com.my.list.domain.ExtraData;
import com.my.list.domain.ExtraDataMapper;

public class SimpleType implements Type {
    
    private String typeName;
    private Class<? extends ExtraData> extraDataClass = null;
    private ExtraDataMapper extraDataMapper = null;

    public static Type nodeType(String typeName) {
        SimpleType simpleType = new SimpleType();
        simpleType.typeName = typeName;
        return simpleType;
    }

    // ---- Getter & Setter ---- //

    public String getTypeName() {
        return typeName;
    }
    public Class<? extends ExtraData> getExtraDataClass() {
        return extraDataClass;
    }
    public ExtraDataMapper getExtraDataMapper() {
        return extraDataMapper;
    }
}
