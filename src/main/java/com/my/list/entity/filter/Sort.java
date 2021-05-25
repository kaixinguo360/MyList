package com.my.list.entity.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.my.list.exception.DataException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sort {

    private String property;
    private Direction direction;

    public enum Direction {
        ASC("asc"),
        DESC("desc");
        
        private String value;
        
        Direction(String value) {
            this.value = value;
        }

        @JsonValue
        public String toString() {
            return value;
        }
        
        @JsonCreator
        public static Direction parse(String value) {
            for (Direction d : Direction.values()) {
                if (d.value.equalsIgnoreCase(value)) {
                    return d;
                }
            }
            throw new DataException("No such sort type, value=" + value);
        }
    }
}
