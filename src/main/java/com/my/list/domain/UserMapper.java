package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends SimpleMapper<User> {
    User selectByNameAndPass(String name, String pass);
}
