package com.my.list.service;

import com.my.list.data.User;
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
            User user1 = new User();
            user1.setName("test");
            user1.setPassword(userService.encrypt("123"));
            userService.addUser(user1);
            User user2 = new User();
            user2.setName("test");
            user2.setPassword(userService.encrypt("1234"));
            userService.addUser(user2);
            assertTrue(true);
        } catch (Exception ignored) {}
        userService.getAllUsers().forEach(user -> System.out.println(user.toString()));
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
