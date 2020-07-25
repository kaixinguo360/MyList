package com.my.list.modules.resource;

import com.my.list.exception.NotImplementedException;
import com.my.list.modules.group.GroupService;
import com.my.list.modules.user.User;
import com.my.list.util.Authorization;
import com.my.list.util.CurrentUser;
import com.my.list.util.SimpleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.my.list.Constants.API_ROOT;

@Authorization
@SimpleController
@RequestMapping(API_ROOT + "/image")
public class ResourceController {
    
    @Autowired private GroupService groupService;
    @Autowired private ResourceService service;


    // ----- Single ----- //

    /**
     * POST /{resource}
     */
    @PostMapping("")
    @Transactional
    public Resource post(@CurrentUser User user, @RequestBody Resource resource) {
        service.create(user, resource);
        return resource;
    }

    /**
     * GET /{resource}/{id}
     */
    @GetMapping("/{id}")
    public Resource get(@CurrentUser User user, @PathVariable Long id) {
        return service.get(user, id);
    }

    /**
     * PUT /{resource}
     */
    @PutMapping("")
    @Transactional
    public Resource put(@CurrentUser User user, @RequestBody Resource resource) {
        service.update(user, resource);
        return resource;
    }

    /**
     * DELETE /{resource}/{id}
     */
    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@CurrentUser User user, @PathVariable Long id) {
        service.delete(user, id);
    }

    // ----- Batch ----- //

    /**
     * POST /{resource}/batch
     */
    @PostMapping("/batch")
    @Transactional
    public List<Resource> batchPost(@CurrentUser User user, @RequestBody List<Resource> resources) {
        return resources.stream()
            .map(resource -> post(user, resource))
            .collect(Collectors.toList());
    }

    /**
     * GET /{resource}/batch?ids=...
     */
    @GetMapping("/batch")
    public List<Resource> batchGet(@CurrentUser User user, @RequestParam List<Long> ids) {
        return ids.stream()
            .map(id -> get(user, id))
            .collect(Collectors.toList());
    }

    /**
     * PUT /{resource}/batch
     */
    @PutMapping("/batch")
    @Transactional
    public List<Resource> batchPut(@CurrentUser User user, @RequestBody List<Resource> resources) {
        return resources.stream()
            .map(resource -> put(user, resource))
            .collect(Collectors.toList());
    }

    /**
     * DELETE /{resource}/batch
     */
    @DeleteMapping("/batch")
    @Transactional
    public void batchDelete(@CurrentUser User user, @RequestParam List<Long> ids) {
        ids.forEach(id -> delete(user, id));
    }
}
