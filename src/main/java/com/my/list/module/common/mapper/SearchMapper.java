package com.my.list.module.common.mapper;

import com.my.list.module.common.Resource;
import com.my.list.system.mapper.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchMapper<T extends Resource> {

    List<T> search(
        @Param("user") User user,
        @Param("andTags") List<String> andTags,
        @Param("orTags") List<String> orTags,
        @Param("notTags") List<String> notTags,
        @Param("includeText") List<String> includeText,
        @Param("excludeText") List<String> excludeText,
        @Param("limit") Integer limit,
        @Param("offset") Integer offset,
        @Param("orderBy") String orderBy,
        @Param("orderDirection") String orderDirection
    );

}
