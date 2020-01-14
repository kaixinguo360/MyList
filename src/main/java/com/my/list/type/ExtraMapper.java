package com.my.list.type;

import java.util.List;

public interface ExtraMapper {
    int deleteByPrimaryKey(Long id);
    int insert(ExtraData record);
    ExtraData selectByPrimaryKey(Long id);
    List<ExtraData> selectAll();
    int updateByPrimaryKey(ExtraData record);
    ExtraData selectByNodeId(Long id);
}
