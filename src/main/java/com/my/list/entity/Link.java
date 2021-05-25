package com.my.list.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Link {

    private Long id;
    private Long parentId;
    private Long contentId;
    private Integer order;
}
