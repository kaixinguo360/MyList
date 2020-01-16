package com.my.list.dto;

public class ListItem {

    public Node node;
    public ItemStatus itemStatus;

    public ListItem(Node node, ItemStatus itemStatus) {
        this.node = node;
        this.itemStatus = itemStatus;
    }

    public enum ItemStatus {
        NEW,        // has all data
        UPDATE,     // has all data
        EXIST,      // only has main data
    }

}
