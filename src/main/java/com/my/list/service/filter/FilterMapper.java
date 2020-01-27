package com.my.list.service.filter;

import com.my.list.domain.Node;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FilterMapper {
    List<Node> getAll(Long userId, Filter filter);
}
