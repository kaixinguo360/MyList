package com.my.list.controller;

import com.my.list.aop.Authorization;
import com.my.list.aop.SimpleController;
import com.my.list.entity.User;
import com.my.list.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/user")
@Authorization(true)
@SimpleController
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Transactional
    public User post(@RequestBody User user) {
        userService.add(user);
        return user;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return userService.get(id);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PutMapping
    @Transactional
    public User put(@RequestBody User user) {
        userService.update(user);
        return user;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable Long id) {
        userService.remove(id);
    }
}
