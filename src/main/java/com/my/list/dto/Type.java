package com.my.list.dto;

import com.my.list.domain.ExtraData;
import com.my.list.domain.ExtraDataMapper;

import javax.validation.constraints.NotNull;

public interface Type {
    String getTypeName();
    default Class<? extends ExtraData> getExtraDataClass() {
        return null;
    }
    default ExtraDataMapper getExtraDataMapper() {
        return null;
    }
    default boolean isHasExtraData() {
        return false;
    }
    default boolean isHasExtraList() {
        return false;
    }
    default boolean isUniqueExtraList() {
        return false;
    }
    default void process(@NotNull Node node) {}
}
