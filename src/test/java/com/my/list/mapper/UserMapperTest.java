package com.my.list.mapper;

import com.my.list.bean.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserMapperTest {
    
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        List<User> users =  userMapper.selectAll();
        System.out.println(users);
    }

}
