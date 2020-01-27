package com.my.list.controller;

import com.my.list.Constants;
import com.my.list.controller.util.Authorization;
import com.my.list.controller.util.CurrentToken;
import com.my.list.controller.util.SimpleController;
import com.my.list.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
        String pass
    ) {
        return userService.generateToken(name, pass);
    }

    @GetMapping("admin")
    public String generateAdminToken(
        String pass
    ) {
        return userService.generateAdminToken(pass);
    }

    @DeleteMapping
    @Authorization
    public void invalidateToken(
        @CurrentToken String token
    ) {
        userService.invalidateToken(token);
    }

    @DeleteMapping("admin")
    @Authorization(true)
    public void invalidateAdminToken(
        @RequestHeader(Constants.AUTHORIZATION) String token
    ) {
        userService.invalidateAdminToken(token);
    }
}
