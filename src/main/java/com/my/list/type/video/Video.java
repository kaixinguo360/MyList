package com.my.list.type.video;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.my.list.entity.ExtraData;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonTypeName(value = "video")
public class Video implements ExtraData {

    private Long id = null;
    private String url = null;
    private String format = null;

}
