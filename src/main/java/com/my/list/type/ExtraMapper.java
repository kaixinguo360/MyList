package com.my.list.type;

import java.util.List;

public interface ExtraMapper {
    int deleteByPrimaryKey(Long id);
    int insert(ExtraValues record);
    ExtraValues selectByPrimaryKey(Long id);
    List<ExtraValues> selectAll();
    int updateByPrimaryKey(ExtraValues record);
    ExtraValues selectByNodeId(Long id);
}
