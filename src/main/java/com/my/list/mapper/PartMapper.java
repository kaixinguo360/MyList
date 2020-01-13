package com.my.list.mapper;

import com.my.list.bean.Part;

import java.util.List;

public interface PartMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Part record);

    Part selectByPrimaryKey(Long id);

    List<Part> selectAll();

    int updateByPrimaryKey(Part record);
}
