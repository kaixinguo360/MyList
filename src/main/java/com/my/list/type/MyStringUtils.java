package com.my.list.type;

public class MyStringUtils {
    public static String limit(String text, int length) {
        if (text == null) return null;
        return text.length() <= length ? text : (text.substring(0, length - 3) + "...");
    }
}
