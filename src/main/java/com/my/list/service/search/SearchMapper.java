package com.my.list.service.search;

import com.my.list.domain.Node;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchMapper {
    List<Node> simpleSearch(List<Condition> conditions, List<Sort> sorts);
}
