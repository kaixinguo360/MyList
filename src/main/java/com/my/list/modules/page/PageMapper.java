package com.my.list.modules.page;

import com.my.list.Limit;
import com.my.list.Order;
import com.my.list.modules.resource.Resource;
import com.my.list.modules.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PageMapper {

    void insert(@Param("user") User user, @Param("resource") Resource resource);
    Resource select(@Param("user") User user, @Param("id") Long id);
    void update(@Param("user") User user, @Param("resource") Resource resource);
    void delete(@Param("user") User user, @Param("id") Long id);

    List<Resource> search(
        @Param("user") User user,
        @Param("includeText") List<String> includeText,
        @Param("excludeText") List<String> excludeText,
        @Param("order") Order order,
        @Param("limit") Limit limit
    );
}
