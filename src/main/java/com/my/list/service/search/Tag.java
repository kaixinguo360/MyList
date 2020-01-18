package com.my.list.service.search;

public class Tag {

    private final boolean strict;
    private final String value;
    private final Long id;

    Tag(Long id) {
        this(id, null, true);
    }
    Tag(String value) {
        this(null, value, true);
    }
    Tag(String value, boolean strict) {
        this(null, value, strict);
    }
    private Tag(Long id, String value, boolean strict) {
        this.strict = strict;
        this.value = value;
        this.id = id;
    }
}
