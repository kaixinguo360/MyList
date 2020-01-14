package com.my.list.dto;

import com.my.list.domain.Node;

import java.util.List;
import java.util.Map;

public class NodeDTO implements ExtraNode {
    
    private final Node node;
    private Map<String, Object> extraData = null;
    private List<ListItem> extraList = null;

    // ---- Constructor ---- //
    public NodeDTO() {
        this.node = new Node();
    }
    public NodeDTO(SingleNode singleNode) {
        this.node = Node.Companion.fromSingleNode(singleNode);
    }

    // ---- Getter of SingleNode ---- //
    public SingleNode getSingleNode() {
        return node;
    }

    // ---- Getter & Setter of ExtraData ---- //
    public Map<String, Object> getExtraData() {
        return extraData;
    }
    public void setExtraData(Map<String, Object> extraData) {
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
