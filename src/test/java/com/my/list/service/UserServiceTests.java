package com.my.list.service;

import com.my.list.data.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Before
    public void addUser() {
        try {
            userService.addUser("test", "123");
            userService.addUser("test", "1234");
            assertTrue(true);
        } catch (Exception ignored) {}
        userService.listUsers().forEach(user -> System.out.println(user.toString()));
    }

    @Test
    public void testCheckUser() {
        assertTrue(userService.checkUser("test", "123"));
        assertFalse(userService.checkUser("test", "1234"));
    }

    @Test
    public void testEncrypt() {
        assertEquals(userService.encrypt("123"), userService.encrypt("123"));
        assertNotEquals(userService.encrypt("123"), userService.encrypt("1234"));
    }
}
