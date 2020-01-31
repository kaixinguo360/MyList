package com.my.list.controller;

import com.my.list.controller.util.Authorization;
import com.my.list.controller.util.SimpleController;
import com.my.list.domain.User;
import com.my.list.service.UserService;
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
    public User put(@RequestBody User user) {
        userService.update(user);
        return user;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.remove(id);
    }
}