package com.my.list.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(as = NodeImpl.class)
public interface Node {

    // ---- Getter of MainData ---- //
    MainData getMainData();

    // ---- Getter & Setter of ExtraData ---- //
    ExtraData getExtraData();
    <T extends ExtraData> T getExtraData(Class<T> extraDataClass);
    void setExtraData(ExtraData extraData);

    // ---- Getter & Setter of ExtraList ---- //
    List<ListItem> getExtraList();
    void setExtraList(List<ListItem> extraList);
    
}
