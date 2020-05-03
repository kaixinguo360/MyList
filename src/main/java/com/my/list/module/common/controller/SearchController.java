package com.my.list.module.common.controller;

import com.my.list.system.mapper.User;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SearchController<T> {
    
    /**
     * GET /{resource}/search?text={text}&tags={tags}
     */
    List<T> search(
        User user,
        @RequestParam(required = false) List<String> andTags,
        @RequestParam(required = false) List<String> orTags,
        @RequestParam(required = false) List<String> notTags,
        @RequestParam(required = false) List<String> includeText,
        @RequestParam(required = false) List<String> excludeText,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) Integer offset
    );
}
