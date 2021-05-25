package com.my.list.type.image;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.my.list.entity.ExtraData;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonTypeName(value = "image")
public class Image implements ExtraData {

    private Long id = null;
    private String url = null;
    private String type = null;
    private String author = null;
    private String gallery = null;
    private String source = null;

}
