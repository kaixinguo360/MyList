package com.my.list.service.interfaces;

import com.my.list.domain.User;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SearchService<T> {
    
    /**
     * GET /{resource}/search?text={text}&tags={tags}
     */
    List<T> search(User user, @RequestParam @Nullable String text, @RequestParam @Nullable String tags);
}
