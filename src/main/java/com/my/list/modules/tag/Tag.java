package com.my.list.modules.tag;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@Data
public class Tag {

    private Long id;
    private Long user;

    private Timestamp ctime;
    private Timestamp mtime;

    private String name;
    private String description;

}
