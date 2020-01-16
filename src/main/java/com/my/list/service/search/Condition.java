package com.my.list.service.search;

class Condition {

    private final String column;
    private final String oper;
    private final Object value;

    Condition(String column, String oper, Object value) {
        this.column = column;
        this.oper = oper;
        this.value = value;
    }
}
