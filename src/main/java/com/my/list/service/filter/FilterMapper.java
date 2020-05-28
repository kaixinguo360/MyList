package com.my.list.service.filter;

import com.my.list.domain.Node;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FilterMapper {
    List<Node> getAll(@Param("userId") Long userId, @Param("filter") Filter filter);
}
