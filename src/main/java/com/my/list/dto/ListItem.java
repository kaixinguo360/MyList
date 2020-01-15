package com.my.list.dto;

public class ListItem {
    
    public ItemStatus itemStatus;
    public ExtraNode extraNode;
    public SingleNode singleNode;

    public ListItem(ExtraNode extraNode) {
        this.extraNode = extraNode;
        itemStatus = (extraNode.getSingleNode().getId() == null) ? ItemStatus.NEW : ItemStatus.UPDATE;
    }
    public ListItem(SingleNode singleNode) {
        this.singleNode = singleNode;
        itemStatus = ItemStatus.EXIST;
    }
    
    public enum ItemStatus {
        NEW,        // use extraNode
        UPDATE,     // use extraNode
        EXIST,      // use singleNode
    }
}
