package com.my.list.domain;

import lombok.Data;

@Data
public class Part {

    private Long id;
    private Long parentId;
    private Long contentId;
    private Integer contentOrder;

    @Override
    public String toString() {
        return "Part{" +
            "id=" + id +
            ", parentId=" + parentId +
            ", contentId=" + contentId +
            ", contentOrder=" + contentOrder +
            '}';
    }
}
