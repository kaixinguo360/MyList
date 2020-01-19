package com.my.list.controller;

import com.my.list.controller.util.Authorization;
import com.my.list.controller.util.CurrentToken;
import com.my.list.controller.util.SimpleResponse;
import com.my.list.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/token")
@RestController
public class TokenController {
    
    private final UserService userService;

    public TokenController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public SimpleResponse generateToken(
        String name,
        String pass,
        Boolean safe
    ) {
        return new SimpleResponse(
            userService.generateToken(name, pass, safe == null || safe)
        );
    }
    
    @DeleteMapping
    @Authorization
    public SimpleResponse invalidateToken(
        @CurrentToken String token
    ) {
        userService.invalidateToken(token);
        return new SimpleResponse();
    }
}
