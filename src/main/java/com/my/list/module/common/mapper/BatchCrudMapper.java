package com.my.list.module.common.mapper;

import com.my.list.module.common.Resource;
import com.my.list.system.mapper.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BatchCrudMapper<T extends Resource> {

    void batchInsert(User user, List<T> resources);
    List<T> batchSelect(User user, List<Long> ids);
    void batchUpdate(User user, List<T> resources);
    void batchDelete(User user, List<Long> ids);

}
