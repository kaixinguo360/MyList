package com.my.list.service;

import com.my.list.data.Token;
import com.my.list.data.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TokenServiceTests {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @Test
    public void test() {
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);

        Token token1 = tokenService.createToken(user1);
        assertTrue(tokenService.checkToken(token1));
        Token token2 = tokenService.createToken(user2);
        assertTrue(tokenService.checkToken(token2));
        Token token3 = tokenService.createToken(user2);
        assertTrue(tokenService.checkToken(token3));

        tokenService.removeToken(token1.getToken());
        assertFalse(tokenService.checkToken(token1));
        tokenService.removeToken(user2);
        assertFalse(tokenService.checkToken(token2));
        assertFalse(tokenService.checkToken(token3));
    }
}
