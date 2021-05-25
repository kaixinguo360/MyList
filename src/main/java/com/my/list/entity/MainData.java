package com.my.list.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MainData {

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

    public static MainData copy(MainData s) {
        if (s == null) return null;
        return new MainData(s.getId(), s.getUser(), s.getType(), s.getCtime(), s.getMtime(),
            s.getTitle(), s.getExcerpt(), s.getPart(), s.getCollection(), s.getPermission(),
            s.getNsfw(), s.getLike(), s.getHide(), s.getSource(), s.getDescription(), s.getComment());
    }

    public static MainData defaultNode() {
        MainData mainData = new MainData();
        mainData.permission = "private";
        mainData.part = false;
        mainData.collection = false;
        mainData.nsfw = false;
        mainData.like = false;
        mainData.hide = false;
        return mainData;
    }
}
