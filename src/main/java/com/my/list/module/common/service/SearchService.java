package com.my.list.module.common.service;

import com.my.list.module.common.Resource;
import com.my.list.system.mapper.User;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SearchService<T extends Resource> {
    
    /**
     * Search Resources
     * GET /{resource}/search?text={text}&tags={tags}
     */
    List<T> search(User user, @RequestParam @Nullable String text, @RequestParam @Nullable String tags);
}
