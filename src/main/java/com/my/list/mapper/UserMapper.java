package com.my.list.mapper;

import com.my.list.entity.User;
import com.my.list.mapper.common.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends CrudMapper<User> {
    User selectByNameAndPass(@Param("name") String name, @Param("pass") String pass);
}
