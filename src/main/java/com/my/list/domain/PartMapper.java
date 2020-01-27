package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PartMapper extends CrudMapper<Part> {
    List<Part> selectByListId(Long id);
    Integer count(Long id);
    void deleteByListId(Long id);
    void deleteByListIdAndPartIds(Long listId, List<Long> partIds);
}
