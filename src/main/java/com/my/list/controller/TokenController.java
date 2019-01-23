package com.my.list.controller;

import com.my.list.Constants;
import com.my.list.data.Token;
import com.my.list.data.User;
import com.my.list.service.TokenService;
import com.my.list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public TokenController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity login(@RequestParam String name, @RequestParam String password) {
        if (!userService.checkUser(name, password)) {
            return new ResponseEntity<>("Incorrect Name Or Password!", HttpStatus.UNAUTHORIZED);
        } else  {
            User user = userService.getUser(name);
            Token token = tokenService.createToken(user);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
    }

    @Authorization
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity logout(HttpServletRequest request) {
        Object currentToken = request.getAttribute(Constants.CURRENT_TOKEN);
        if (currentToken instanceof Token) {
            tokenService.removeToken(((Token) currentToken).getToken());
            return new ResponseEntity<>("Success!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
