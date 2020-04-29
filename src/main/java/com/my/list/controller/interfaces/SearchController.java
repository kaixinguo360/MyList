package com.my.list.controller.interfaces;

import com.my.list.domain.User;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SearchController<T> {
    
    /**
     * GET /{resource}/search?text={text}&tags={tags}
     */
    List<T> search(User user, @RequestParam @Nullable String text, @RequestParam @Nullable String tags);
}
