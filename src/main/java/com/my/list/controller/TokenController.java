package com.my.list.controller;

import com.my.list.Constants;
import com.my.list.data.Token;
import com.my.list.data.User;
import com.my.list.service.DataException;
import com.my.list.service.TokenService;
import com.my.list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/tokens")
public class TokenController {

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public TokenController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Token login(String name, String password) throws RequestException, DataException {
        if (!userService.checkUser(name, password)) {
            throw new RequestException("Incorrect Name Or Password!", HttpStatus.UNAUTHORIZED);
        } else  {
            User user = userService.getUser(name);
            return tokenService.createToken(user);
        }
    }

    @Authorization
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE)
    public MessageResponse logout(HttpServletRequest request) throws RequestException {
        Object currentToken = request.getAttribute(Constants.CURRENT_TOKEN);
        if (currentToken instanceof Token) {
            tokenService.removeToken(((Token) currentToken).getToken());
            return new MessageResponse("Logout Successful");
        } else {
            throw new RequestException("An Error Occur", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
