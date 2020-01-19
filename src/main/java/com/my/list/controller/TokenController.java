package com.my.list.controller;

import com.my.list.controller.util.Authorization;
import com.my.list.controller.util.CurrentToken;
import com.my.list.controller.util.SimpleController;
import com.my.list.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/token")
@SimpleController
public class TokenController {
    
    private final UserService userService;

    public TokenController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String generateToken(
        String name,
        String pass,
        Boolean safe
    ) {
        return userService.generateToken(name, pass, safe == null || safe);
    }
    
    @DeleteMapping
    @Authorization
    public void invalidateToken(
        @CurrentToken String token
    ) {
        userService.invalidateToken(token);
    }
}
