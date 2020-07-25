package com.my.list.module.common.controller;

import com.my.list.exception.NotImplementedException;
import com.my.list.module.common.Resource;
import com.my.list.modules.user.User;
import com.my.list.util.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SearchController<T extends Resource> extends BaseController<T> {

    /**
     * GET /{resource}?params=...
     */
    @GetMapping("")
    default List<T> search(
        @CurrentUser User user,
        @RequestParam(required = false) List<String> andTags,
        @RequestParam(required = false) List<String> orTags,
        @RequestParam(required = false) List<String> notTags,
        @RequestParam(required = false) List<String> includeText,
        @RequestParam(required = false) List<String> excludeText,
        @RequestParam(required = false, defaultValue = "100") Integer limit,
        @RequestParam(required = false, defaultValue = "0") Integer offset,
        @RequestParam(required = false, name = "order", defaultValue = "ctime") String orderBy,
        @RequestParam(required = false, name = "direction", defaultValue = "asc") String orderDirection
    ) {
        switch (orderDirection.toLowerCase()) {
            case "asc":
                orderDirection = "asc"; break;
            case "desc":
                orderDirection = "desc"; break;
            default:
                throw new NotImplementedException("OrderDirection[" + orderDirection + "]");
        }
        return getSearchService().search(user, andTags, orTags, notTags, includeText, excludeText, limit, offset, orderBy, orderDirection);
    }
    
}
