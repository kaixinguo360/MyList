package com.my.list.service.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.my.list.exception.DataException;

public class Sort {

    String property;
    Direction direction;

    Sort() {}
    Sort(String property, Direction direction) {
        this.property = property;
        this.direction = direction;
    }

    // ---- Setter & Getter ---- //
    public String getProperty() {
        return property;
    }
    public void setProperty(String property) {
        this.property = property;
    }
    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

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
