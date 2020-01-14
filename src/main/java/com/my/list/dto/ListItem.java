package com.my.list.dto;

public class ListItem {
    
    private ItemStatus itemStatus;
    ExtraNode extraNode;
    
    public enum ItemStatus {
        TO_CREATE,
        EXIST,
        NOT_FOUND
    }
}
