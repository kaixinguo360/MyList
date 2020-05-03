package com.my.list.module.common.controller;

import com.my.list.system.mapper.User;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface GroupController<T> {

    /**
     * GET /{resource}/group/{id}
     */
    List<T> getByGroup(User user, @PathVariable Long groupId);
}
