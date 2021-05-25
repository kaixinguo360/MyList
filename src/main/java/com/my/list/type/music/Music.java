package com.my.list.type.music;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.my.list.entity.ExtraData;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonTypeName(value = "music")
public class Music implements ExtraData {

    private Long id = null;
    private String url = null;
    private String format = null;

}
