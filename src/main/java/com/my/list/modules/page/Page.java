package com.my.list.modules.page;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Page {

    private String url;
    private String title;
    private String description;
    
    private Long user;
    private Timestamp ctime;
    private Timestamp mtime;
}
