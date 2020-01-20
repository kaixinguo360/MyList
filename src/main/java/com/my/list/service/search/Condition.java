package com.my.list.service.search;

class Condition {

    private String column;
    private String oper;
    private Object value;

    Condition() {}
    Condition(String column, String oper, Object value) {
        this.column = column;
        this.oper = oper;
        this.value = value;
    }

    // ---- Setter & Getter ---- //
    public String getColumn() {
        return column;
    }
    public void setColumn(String column) {
        this.column = column;
    }
    public String getOper() {
        return oper;
    }
    public void setOper(String oper) {
        this.oper = oper;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
}
