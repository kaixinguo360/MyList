package com.my.list.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "nodeType")
@JsonIgnoreProperties({"id"})
public interface ExtraData {
    Long getId();
    void setId(Long id);
}
