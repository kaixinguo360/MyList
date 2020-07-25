package com.my.list.modules.resource;

import com.my.list.modules.page.Page;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Resource {

    private Long id;
    private String data;
    private String title;
    private String description;
    
    private Page page;
    private List<String> tags;
    
    private Long user;
    private Timestamp ctime;
    private Timestamp mtime;
}
