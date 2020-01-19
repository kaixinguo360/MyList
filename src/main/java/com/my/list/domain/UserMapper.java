package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends CrudMapper<User> {
    User selectByNameAndPass(String name, String pass);
    User selectByNameAndPassUnsafe(String name, String pass);
}
