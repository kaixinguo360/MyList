package com.my.list.system.controller;

import com.my.list.system.mapper.Tag;
import com.my.list.system.mapper.User;
import com.my.list.system.service.TagService;
import com.my.list.util.Authorization;
import com.my.list.util.CurrentUser;
import com.my.list.util.SimpleController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.my.list.Constants.API_ROOT;

@Authorization
@SimpleController
@RequestMapping(API_ROOT + "/tag")
@Slf4j
public class TagController {
    
    @Autowired TagService service;

    // ----- Single CRUD ----- //

    /**
     * POST /tag
     */
    @PostMapping("")
    @Transactional
    public Object post(@CurrentUser User user, @RequestBody Tag resource) {
        service.create(user, resource);
        return resource;
    }

    /**
     * GET /tag/{name}
     */
    @GetMapping("/{name}")
    public Object get(@CurrentUser User user, @PathVariable String name) {
        return service.get(user, name);
    }

    /**
     * PUT /tag
     */
    @PutMapping("")
    @Transactional
    public Object put(@CurrentUser User user, @RequestBody Tag resource) {
        service.update(user, resource);
        return resource;
    }

    /**
     * DELETE /tag/{name}
     */
    @DeleteMapping("/{name}")
    @Transactional
    public void delete(@CurrentUser User user, @PathVariable String name) {
        service.delete(user, name);
    }
    
    // ----- Search ----- //

    /**
     * GET /{resource}/search?text={text}&tags={tags}
     */
    @GetMapping("")
    List<Tag> search(
        User user,
        @RequestParam(required = false) List<String> includeText,
        @RequestParam(required = false) List<String> excludeText,
        @RequestParam(required = false, defaultValue = "100") Integer limit,
        @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        return service.search(user, includeText, excludeText, limit, offset);
    }
}
