package com.my.list.service;

import com.my.list.type.ExtraData;
import com.my.list.type.ExtraMapper;

public class Type {
    
    public final String typeName;
    public final boolean hasExtraList;
    public final Class<? extends ExtraData> extraDataClass;
    public final ExtraMapper extraMapper;

    public Type(String typeName) {
        this(typeName, null, null);
    }
    public Type(String typeName, boolean hasExtraList) {
        this(typeName, hasExtraList, null, null);
    }
    public Type(String typeName, Class<? extends ExtraData> extraDataClass, ExtraMapper extraMapper) {
        this(typeName, false, extraDataClass, extraMapper);
    }
    public Type(String typeName, boolean hasExtraList, Class<? extends ExtraData> extraDataClass, ExtraMapper extraMapper) {
        this.typeName = typeName;
        this.hasExtraList = hasExtraList;
        this.extraDataClass = extraDataClass;
        this.extraMapper = extraMapper;
    }

    boolean hasExtraData() {
        return extraDataClass != null;
    }
    boolean hasExtraList() {
        return hasExtraList;
    }
}
