package com.my.list.service.search;

import com.my.list.service.DataException;

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

    private String value;

    Permission(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
    
    public static Permission parse(String value) {
        for (Permission p : Permission.values()) {
            if (p.value.equalsIgnoreCase(value)) {
                return p;
            }
        }
        throw new DataException("No such permission type, value=" + value);
    }
}
