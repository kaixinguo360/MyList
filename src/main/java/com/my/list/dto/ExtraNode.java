package com.my.list.dto;

import java.util.List;
import java.util.Map;

public interface ExtraNode {

    // ---- Getter of SingleNode ---- //
    SingleNode getSingleNode();

    // ---- Getter & Setter of ExtraData ---- //
    Map<String, Object> getExtraData();
    void setExtraData(Map<String, Object> extraData);

    // ---- Getter & Setter of ExtraList ---- //
    List<ListItem> getExtraList();
    void setExtraList(List<ListItem> extraList);
    
}
