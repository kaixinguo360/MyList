package com.my.list.dto;

import com.my.list.domain.ExtraData;
import com.my.list.domain.MainData;

import java.util.List;

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