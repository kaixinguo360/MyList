package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends CrudMapper<User> {
    User selectByNameAndPass(@Param("name") String name, @Param("pass") String pass);
}
