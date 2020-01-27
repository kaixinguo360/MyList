package com.my.list.dto;

import com.my.list.domain.ExtraData;
import com.my.list.domain.ExtraDataMapper;

public class SimpleType implements Type {
    
    private String typeName;
    private Class<? extends ExtraData> extraDataClass = null;
    private ExtraDataMapper extraDataMapper = null;
    private boolean hasExtraData = false;
    private boolean hasExtraList = false;
    private boolean extraListUnique = false;
    private boolean extraListRequired = true;

    public static Type nodeType(String typeName) {
        SimpleType simpleType = new SimpleType();
        simpleType.typeName = typeName;
        return simpleType;
    }
    public static Type postType(String typeName) {
        SimpleType simpleType = new SimpleType();
        simpleType.typeName = typeName;
        simpleType.hasExtraList = true;
        return simpleType;
    }
    public static Type tagType(String typeName) {
        SimpleType simpleType = new SimpleType();
        simpleType.typeName = typeName;
        simpleType.hasExtraList = true;
        simpleType.extraListUnique = true;
        simpleType.extraListRequired = false;
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
    public boolean isHasExtraData() {
        return hasExtraData;
    }
    public boolean isHasExtraList() {
        return hasExtraList;
    }
    public boolean isExtraListUnique() {
        return extraListUnique;
    }
    public boolean isExtraListRequired() {
        return extraListRequired;
    }
}
