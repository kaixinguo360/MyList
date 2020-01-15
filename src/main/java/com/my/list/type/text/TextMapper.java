package com.my.list.type.text;

import com.my.list.type.ExtraDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TextMapper extends ExtraDataMapper {
    @Select("select * from texts where text_node_id = #{id,jdbcType=BIGINT}")
    @ResultMap("BaseResultMap")
    Text selectByNodeId(Long id);
}
