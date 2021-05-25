package com.my.list.type.dlist;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.my.list.entity.ExtraData;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonTypeName(value = "dlist")
public class DList implements ExtraData {

    private Long id = null;
    private String filter = null;
    private String config = null;

}
