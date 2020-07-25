package com.my.list.module.common.mapper;

import com.my.list.module.common.Resource;
import com.my.list.modules.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SingleCrudMapper<T extends Resource> {
    
    void insert(@Param("user") User user, @Param("resource") T resource);
    T select(@Param("user") User user, @Param("id") Long id);
    void update(@Param("user") User user, @Param("resource") T resource);
    void delete(@Param("user") User user, @Param("id") Long id);

}
