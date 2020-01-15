package com.my.list.type;

import com.my.list.domain.SimpleMapper;

public interface ExtraDataMapper extends SimpleMapper<ExtraData> {
    int updateByPrimaryKey(ExtraData record);
    ExtraData selectByNodeId(Long id);
}
