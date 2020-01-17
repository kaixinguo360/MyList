package com.my.list.service.search;

public enum Permission {
    PRIVATE("private"),
    PROTECT("protect"),
    PUBLIC("public"),
    SHARED("shared"),                   // = PROTECT + PUBLIC
    SELF("self"),                       // = PRIVATE + PROTECT + PUBLIC
    OTHERS_PROTECT("others_protect"),
    OTHERS_PUBLIC("others_public"),
    OTHERS_SHARED("others_shared"),     // = OTHERS_PROTECT + OTHERS_PUBLIC
    EDITABLE("editable"),               // = SELF + OTHERS_PUBLIC
    AVAILABLE("available");             // = SELF + OTHERS_PROTECT + OTHERS_PUBLIC

    private String sql;

    Permission(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}
