package com.my.list.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.my.list.exception.DataException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListItem {

    public Node node;
    public ItemStatus status;

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
