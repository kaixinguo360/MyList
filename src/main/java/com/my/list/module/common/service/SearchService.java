package com.my.list.module.common.service;

import com.my.list.module.common.Resource;
import com.my.list.modules.user.User;

import java.util.List;

public interface SearchService<T extends Resource> extends BaseService<T> {

    /**
     * GET /{resource}
     */
    default List<T> search(
        User user,
        List<String> andTags,
        List<String> orTags,
        List<String> notTags,
        List<String> includeText,
        List<String> excludeText,
        Integer limit,
        Integer offset,
        String orderBy,
        String orderDirection
    ) {
        return getSearchMapper().search(user, andTags, orTags, notTags, includeText, excludeText, limit, offset, orderBy, orderDirection);
    }
}
