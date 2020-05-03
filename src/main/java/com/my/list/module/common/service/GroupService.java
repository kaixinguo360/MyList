package com.my.list.module.common.service;

import com.my.list.module.common.Resource;
import com.my.list.system.mapper.User;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface GroupService<T extends Resource> {

    /**
     * Get Resources By Group
     * GET /{resource}/group/{id}
     */
    List<T> getByGroup(User user, @PathVariable Long groupId);
}
