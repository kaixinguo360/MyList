package com.my.list.service.interfaces;

import com.my.list.domain.User;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface GroupService<T> {

    /**
     * Get Resources By Group
     * GET /{resource}/group/{id}
     */
    List<T> getByGroup(User user, @PathVariable Long groupId);
}
