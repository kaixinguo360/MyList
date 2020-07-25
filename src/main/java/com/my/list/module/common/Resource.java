package com.my.list.module.common;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Resource {
    private Long id;
    private Long user;
    private Timestamp ctime;
    private Timestamp mtime;
}
