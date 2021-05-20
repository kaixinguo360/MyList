package com.my.list.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.sql.Timestamp;

@JsonDeserialize(as = Node.class)
public interface MainData {
    
    void setId(Long id);
    Long getId();
    
    void setUser(Long user);
    Long getUser();
    
    void setType(String type);
    String getType();
    
    void setCtime(Timestamp ctime);
    Timestamp getCtime();
    
    void setMtime(Timestamp mtime);
    Timestamp getMtime();
    
    void setTitle(String title);
    String getTitle();
    
    void setExcerpt(String excerpt);
    String getExcerpt();
    
    void setPart(Boolean part);
    Boolean getPart();
    
    void setCollection(Boolean collection);
    Boolean getCollection();
    
    void setPermission(String permission);
    String getPermission();
    
    void setNsfw(Boolean nsfw);
    Boolean getNsfw();
    
    void setLike(Boolean like);
    Boolean getLike();
    
    void setHide(Boolean hide);
    Boolean getHide();
    
    void setSource(String source);
    String getSource();
    
    void setDescription(String description);
    String getDescription();
    
    void setComment(String comment);
    String getComment();
}
