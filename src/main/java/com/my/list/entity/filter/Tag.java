package com.my.list.entity.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    private Long id;
    private String value;
    private boolean strict;
    private String type;

    public Tag(Long id) {
        this(id, null, true, null);
    }
    public Tag(String value) {
        this(null, value, true, "tag");
    }
    public Tag(String value, boolean strict) {
        this(null, value, strict, "tag");
    }
}
