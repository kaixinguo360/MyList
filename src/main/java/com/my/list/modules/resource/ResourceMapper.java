package com.my.list.modules.resource;

import com.my.list.modules.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResourceMapper {

    void insert(@Param("user") User user, @Param("resource") Resource resource);
    Resource select(@Param("user") User user, @Param("id") Long id);
    void update(@Param("user") User user, @Param("resource") Resource resource);
    void delete(@Param("user") User user, @Param("id") Long id);

    void addTags(@Param("id") Long id, @Param("tagIds") List<Long> tagIds);
    void removeTags(@Param("id") Long id, @Param("tagIds") List<Long> tagIds);
    List<String> getTags(@Param("id") Long id);
    void clearTags(@Param("id") Long id);

    List<Resource> search(
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
