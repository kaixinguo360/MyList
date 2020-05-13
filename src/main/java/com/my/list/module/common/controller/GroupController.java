package com.my.list.module.common.controller;

import com.my.list.system.mapper.Group;
import com.my.list.system.mapper.User;
import com.my.list.system.service.GroupService;
import com.my.list.util.CurrentUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GroupController {

    String getTypeName();

    GroupService getGroupService();

    // ----- Single ----- //

    /**
     * POST /{resource}/group
     */
    @PostMapping("/group")
    @Transactional
    default Group post(@CurrentUser User user, @RequestBody Group group) {
        getGroupService().create(user, getTypeName(), group);
        return group;
    }

    /**
     * GET /{resource}/group/{name}
     */
    @GetMapping("/group/{name}")
    default Group get(@CurrentUser User user, @PathVariable String name) {
        return getGroupService().get(user, getTypeName(), name);
    }

    /**
     * PUT /{resource}/group
     */
    @PutMapping("/group")
    @Transactional
    default Group put(@CurrentUser User user, @RequestBody Group group) {
        getGroupService().update(user, getTypeName(), group);
        return group;
    }

    /**
     * DELETE /{resource}/group/{name}
     */
    @DeleteMapping("/group/{name}")
    @Transactional
    default void delete(@CurrentUser User user, @PathVariable String name) {
        getGroupService().delete(user, getTypeName(), name);
    }

    // ----- getAll ----- //

    /**
     * GET /{resource}/group/{name}
     */
    @GetMapping("/group")
    default List<Group> getAll(
        @CurrentUser User user,
        @RequestParam(required = false, defaultValue = "100") Integer limit,
        @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        return getGroupService().getAll(user, getTypeName(), limit, offset);
    }

}
