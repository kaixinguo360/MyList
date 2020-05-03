package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    // ---- Single Crud ----- //
    void insert(User record);
    User select(Long id);
    List<User> selectAll();
    void update(User record);
    void delete(Long id);

    // ---- Other ----- //
    User selectByNameAndPass(String name, String pass);
}
