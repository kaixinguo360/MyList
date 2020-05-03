package com.my.list.module.tag;

import com.my.list.module.common.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Tag extends Resource {

    private String title;
    private String description;

}
