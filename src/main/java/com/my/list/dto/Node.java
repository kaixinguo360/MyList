package com.my.list.dto;

import com.my.list.type.ExtraValues;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Node {
    
    private com.my.list.domain.Node node;
    private Map<String, Object> extraValues;

    // ---- Constructor ---- //
    public Node() {
        this.node = new com.my.list.domain.Node();
        this.extraValues = new HashMap<>();
    }
    public Node(com.my.list.domain.Node node) {
        this.node = node;
        this.extraValues = new HashMap<>();
    }
    public Node(com.my.list.domain.Node node, ExtraValues extraValues) {
        this.node = node;
        this.extraValues = (extraValues == null) ? new HashMap<>() : extraValues.toMap();
    }

    // ---- Getter & Setter of DTO ---- //
    public com.my.list.domain.Node getDomain() { return node; }
    public void setDomain(com.my.list.domain.Node node) { this.node = node; }
    public Map<String, Object> getExtraValues() { return extraValues; }
    public void setExtraValues(Map<String, Object> extraValues) { this.extraValues = extraValues; }
    
    // ---- Getter & Setter of Domain ---- //
    public Long getId() { return node.getId(); }
    public void setId(Long id) { node.setId(id); }
    public Long getUser() { return node.getUser(); }
    public void setUser(Long user) { node.setUser(user); }
    public String getType() { return node.getType(); }
    public void setType(String type) { node.setType(type); }
    public Timestamp getCtime() { return node.getCtime(); }
    public void setCtime(Timestamp ctime) { node.setCtime(ctime); }
    public Timestamp getMtime() { return node.getMtime(); }
    public void setMtime(Timestamp mtime) { node.setMtime(mtime); }
    public String getTitle() { return node.getTitle(); }
    public void setTitle(String title) { node.setTitle(title); }
    public String getExcerpt() { return node.getExcerpt(); }
    public void setExcerpt(String excerpt) { node.setExcerpt(excerpt); }
    public String getLstatus() { return node.getLstatus(); }
    public void setLstatus(String lstatus) { node.setLstatus(lstatus); }
    public Integer getLcount() { return node.getLcount(); }
    public void setLcount(Integer lcount) { node.setLcount(lcount); }
    public String getPermissions() { return node.getPermissions(); }
    public void setPermissions(String permissions) { node.setPermissions(permissions); }
    public Boolean getNsfw() { return node.getNsfw(); }
    public void setNsfw(Boolean nsfw) { node.setNsfw(nsfw); }
    public Boolean getLike() { return node.getLike(); }
    public void setLike(Boolean like) { node.setLike(like); }
    public String getSourceUrl() { return node.getSourceUrl(); }
    public void setSourceUrl(String sourceUrl) { node.setSourceUrl(sourceUrl); }
    public String getComment() { return node.getComment(); }
    public void setComment(String comment) { node.setComment(comment); }
}
