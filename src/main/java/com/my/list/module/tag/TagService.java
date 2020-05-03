package com.my.list.module.tag;

import com.my.list.module.common.BaseResourceService;
import org.springframework.stereotype.Service;

@Service
public class TagService extends BaseResourceService<Tag> {
    public TagService(TagMapper mapper) {
        super(mapper);
    }
}
