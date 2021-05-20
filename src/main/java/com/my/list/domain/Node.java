package com.my.list.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node implements MainData {

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

    public static Node fromSingleNode(MainData s) {
        if (s == null) return null;
        if (s instanceof Node) return (Node) s;
        return new Node(s.getId(), s.getUser(), s.getType(), s.getCtime(), s.getMtime(),
            s.getTitle(), s.getExcerpt(), s.getPart(), s.getCollection(), s.getPermission(),
            s.getNsfw(), s.getLike(), s.getHide(), s.getSource(), s.getDescription(), s.getComment());
    }

    public static Node defaultNode() {
        Node node = new Node();
        node.permission = "private";
        node.part = false;
        node.collection = false;
        node.nsfw = false;
        node.like = false;
        node.hide = false;
        return node;
    }

    @Override
    public String toString() {
        return "Node{" +
            "id=" + id +
            ", user=" + user +
            ", type='" + type + '\'' +
            ", ctime=" + ctime +
            ", mtime=" + mtime +
            ", title='" + title + '\'' +
            ", excerpt='" + excerpt + '\'' +
            ", part=" + part +
            ", collection=" + collection +
            ", permission='" + permission + '\'' +
            ", nsfw=" + nsfw +
            ", like=" + like +
            ", hide=" + hide +
            ", source='" + source + '\'' +
            ", description='" + description + '\'' +
            ", comment='" + comment + '\'' +
            '}';
    }
}
