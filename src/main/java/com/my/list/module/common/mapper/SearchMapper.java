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
        @Param("andTags") List<Long> andTags,
        @Param("orTags") List<Long> orTags,
        @Param("notTags") List<Long> notTags,
        @Param("includeText") List<String> includeText,
        @Param("excludeText") List<String> excludeText,
        @Param("limit") Integer limit,
        @Param("offset") Integer offset
    );

}
