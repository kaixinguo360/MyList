package com.my.list.dto;

import com.my.list.domain.ExtraData;
import com.my.list.domain.ExtraDataMapper;

public class SimpleType implements Type {
    
    private final String typeName;
    private final Class<? extends ExtraData> extraDataClass;
    private final ExtraDataMapper extraDataMapper;
    private final boolean hasExtraData;
    private final boolean hasExtraList;

    public SimpleType(String typeName) {
        this(typeName, null, null);
    }
    public SimpleType(String typeName, boolean hasExtraList) {
        this(typeName, hasExtraList, null, null);
    }
    public SimpleType(String typeName, Class<? extends ExtraData> extraDataClass, ExtraDataMapper extraDataMapper) {
        this(typeName, false, extraDataClass, extraDataMapper);
    }
    public SimpleType(String typeName, boolean hasExtraList, Class<? extends ExtraData> extraDataClass, ExtraDataMapper extraDataMapper) {
        this.typeName = typeName;
        this.extraDataClass = extraDataClass;
        this.extraDataMapper = extraDataMapper;
        this.hasExtraData = (extraDataClass != null);
        this.hasExtraList = hasExtraList;
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
}
