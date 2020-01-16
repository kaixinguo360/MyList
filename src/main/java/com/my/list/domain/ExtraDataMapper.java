package com.my.list.domain;

public interface ExtraDataMapper extends CrudMapper<ExtraData> {
    ExtraData selectByNodeId(Long id);
}
