package com.my.list.type.image;

import com.my.list.type.ExtraDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ImageMapper extends ExtraDataMapper {
    @Select("select * from images where image_node_id = #{id,jdbcType=BIGINT}")
    @ResultMap("BaseResultMap")
    Image selectByNodeId(Long id);
}
