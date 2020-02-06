package com.my.list.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.my.list.exception.DataException;

public class ListItem {

    public Node node;
    public ItemStatus status;

    public ListItem() {}
    public ListItem(Node node, ItemStatus status) {
        this.node = node;
        this.status = status;
    }

    // ---- Setter & Getter ---- //
    public Node getNode() {
        return node;
    }
    public void setNode(Node node) {
        this.node = node;
    }
    public ItemStatus getStatus() {
        return status;
    }
    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public enum ItemStatus {
        NEW("new"),         // has all data
        UPDATE("update"),   // has all data
        EXIST("exist");          // only has main data

        private String value;

        ItemStatus(String value) {
            this.value = value;
        }

        @JsonValue
        public String toString() {
            return value;
        }

        @JsonCreator
        public static ItemStatus parse(String value) {
            for (ItemStatus i : ItemStatus.values()) {
                if (i.value.equalsIgnoreCase(value)) {
                    return i;
                }
            }
            throw new DataException("No such status type, value=" + value);
        }
    }

}
