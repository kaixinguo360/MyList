package com.my.list.dto;

import com.my.list.domain.ExtraData;
import com.my.list.domain.ExtraDataMapper;

public class Type {
    
    public final String typeName;
    public final Class<? extends ExtraData> extraDataClass;
    public final ExtraDataMapper extraDataMapper;
    public final boolean hasExtraData;
    public final boolean hasExtraList;

    public Type(String typeName) {
        this(typeName, null, null);
    }
    public Type(String typeName, boolean hasExtraList) {
        this(typeName, hasExtraList, null, null);
    }
    public Type(String typeName, Class<? extends ExtraData> extraDataClass, ExtraDataMapper extraDataMapper) {
        this(typeName, false, extraDataClass, extraDataMapper);
    }
    public Type(String typeName, boolean hasExtraList, Class<? extends ExtraData> extraDataClass, ExtraDataMapper extraDataMapper) {
        this.typeName = typeName;
        this.extraDataClass = extraDataClass;
        this.extraDataMapper = extraDataMapper;
        this.hasExtraData = (extraDataClass != null);
        this.hasExtraList = hasExtraList;
    }
}
