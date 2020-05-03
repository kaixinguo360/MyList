package com.my.list.module.tag;

import com.my.list.module.common.BaseResourceController;
import com.my.list.util.Authorization;
import com.my.list.util.SimpleController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.my.list.Constants.API_ROOT;

@Authorization
@SimpleController
@RequestMapping(API_ROOT + "/tag")
@Slf4j
public class TagController extends BaseResourceController<Tag> {
    public TagController(TagService service) {
        super(service);
    }
}
