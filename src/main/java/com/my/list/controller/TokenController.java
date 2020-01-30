package com.my.list.controller;

import com.my.list.Constants;
import com.my.list.controller.util.Authorization;
import com.my.list.controller.util.CurrentToken;
import com.my.list.controller.util.SimpleController;
import com.my.list.domain.User;
import com.my.list.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/token")
@SimpleController
public class TokenController {
    
    private final UserService userService;

    public TokenController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Map<String, Object> generateToken(
        String name,
        String pass
    ) {
        String token = userService.generateToken(name, pass);
        User user = userService.getUserContext(token).user;
        user.setPass(null);
        
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", user);
        return map;
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
