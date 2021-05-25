package com.my.list.type.text;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.my.list.entity.ExtraData;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonTypeName(value = "text")
public class Text implements ExtraData {

    private Long id = null;
    private String content = null;

}
