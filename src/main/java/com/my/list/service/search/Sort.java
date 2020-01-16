package com.my.list.service.search;

public class Sort {

    final String property;
    final Direction direction;

    Sort(String property, Direction direction) {
        this.property = property;
        this.direction = direction;
    }
    
    public enum Direction {
        ASC("asc"),
        DESC("desc");
        
        private String sql;
        
        Direction(String sql) {
            this.sql = sql;
        }

        @Override
        public String toString() {
            return sql;
        }
    }
}
