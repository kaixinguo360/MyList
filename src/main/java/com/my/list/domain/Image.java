package com.my.list.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Image {
    private Long id;
    private Long user;
    private String type;
    private Timestamp ctime;
    private Timestamp mtime;
    private String title;
    private String excerpt;
    private Boolean part;
    private Boolean collection;
    private String permission;
    private Boolean nsfw;
    private Boolean like;
    private Boolean hide;
    private String source;
    private String description;
    private String comment;
}
