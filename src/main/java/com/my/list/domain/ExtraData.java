package com.my.list.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonIgnoreProperties({"extraId", "id"})
public interface ExtraData {
    Long getExtraId();
    void setExtraId(Long id);
    Map<String, Object> toMap();
}
