package com.my.list.controller;

import com.my.list.Constants;
import com.my.list.aop.Authorization;
import com.my.list.aop.CurrentToken;
import com.my.list.aop.SimpleController;
import com.my.list.entity.User;
import com.my.list.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/token")
@SimpleController
public class TokenController {
    
    static class OutputWarp {
        private String token;
        private User user;

        public String getToken() {
            return token;
        }
        public User getUser() {
            return user;
        }
    }
    
    private final UserService userService;

    public TokenController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public OutputWarp generateToken(
        String name,
        String pass
    ) {
        String token = userService.generateToken(name, pass);
        User user = userService.getUserContext(token).user;
        user.setPass(null);

        OutputWarp output = new OutputWarp();
        output.token = token;
        output.user = user;
        return output;
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
