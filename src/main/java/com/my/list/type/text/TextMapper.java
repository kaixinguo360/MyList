package com.my.list.type.text;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TextMapper {
    int deleteByPrimaryKey(Long id);
    int insert(Text record);
    Text selectByPrimaryKey(Long id);
    List<Text> selectAll();
    int updateByPrimaryKey(Text record);
    
    @Select("select * from texts where text_node_id = #{id,jdbcType=BIGINT}")
    @ResultMap("BaseResultMap")
    Text selectByNodeId(Long id);
}
