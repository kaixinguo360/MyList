package com.my.list.dto;

import com.my.list.domain.ExtraData;
import com.my.list.domain.MainData;
import com.my.list.exception.DataException;

import java.util.List;

public class NodeDTO implements Node {
    
    private com.my.list.domain.Node node;
    private ExtraData extraData = null;
    private List<ListItem> extraList = null;

    // ---- Constructor ---- //
    public NodeDTO() {
        this.node = new com.my.list.domain.Node();
    }
    public NodeDTO(MainData mainData) {
        this.node = com.my.list.domain.Node.fromSingleNode(mainData);
    }

    // ---- Getter & Setter of SingleNode ---- //
    public MainData getMainData() {
        return node;
    }
    public void setMainData(MainData node) {
        this.node = (com.my.list.domain.Node) node;
    }

    // ---- Getter & Setter of ExtraData ---- //
    public ExtraData getExtraData() {
        return extraData;
    }
    public <T extends ExtraData> T getExtraData(Class<T> extraDataClass) {
        if (extraData == null) throw new DataException("ExtraData is null.");
        @SuppressWarnings("unchecked") T t = (T) extraData;
        return t;
    }
    public void setExtraData(ExtraData extraData) {
        this.extraData = extraData;
    }

    // ---- Getter & Setter of ExtraList ---- //
    public List<ListItem> getExtraList() {
        return extraList;
    }
    public void setExtraList(List<ListItem> extraList) {
        this.extraList = extraList;
    }
    
}
