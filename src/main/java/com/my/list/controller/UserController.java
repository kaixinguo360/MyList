package com.my.list.controller;

import com.my.list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Authorization
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/users/check", method = RequestMethod.POST)
    public MessageResponse checkUser(@RequestParam String name, @RequestParam String password) {
        if(name != null && password != null && userService.checkUser(name, password))
            return new MessageResponse("Success!");
        else
            return new MessageResponse("Error!");
    }

}
