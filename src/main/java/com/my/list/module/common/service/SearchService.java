package com.my.list.module.common.service;

import com.my.list.module.common.Resource;
import com.my.list.system.mapper.User;

import java.util.List;

public interface SearchService<T extends Resource> {
    
    /**
     * Search Resources
     * GET /{resource}?param1={param1}&param2={param2}...
     */
    List<T> search(
        User user,
        List<String> andTags,
        List<String> orTags,
        List<String> notTags,
        List<String> includeText,
        List<String> excludeText,
        Integer limit,
        Integer offset
    );
}
