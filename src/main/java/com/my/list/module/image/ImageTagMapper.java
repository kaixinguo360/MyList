package com.my.list.module.image;

import com.my.list.module.common.mapper.ResourceTagMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageTagMapper extends ResourceTagMapper {

    void addTag(
        @Param("resourceId") Long resourceId,
        @Param("tagId") Long tagId
    );

    void removeTag(
        @Param("resourceId") Long resourceId,
        @Param("tagId") Long tagId
    );

    List<String> getTags(
        @Param("resourceId") Long resourceId
    );
}
