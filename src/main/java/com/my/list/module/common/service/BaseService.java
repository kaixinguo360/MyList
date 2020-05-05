package com.my.list.module.common.service;

import com.my.list.module.common.Resource;
import com.my.list.module.common.mapper.BatchCrudMapper;
import com.my.list.module.common.mapper.SearchMapper;
import com.my.list.module.common.mapper.SingleCrudMapper;
import com.my.list.module.common.mapper.TagEditMapper;
import com.my.list.system.service.TagService;

public interface BaseService<T extends Resource> {

    TagService getTagService();

    SingleCrudMapper<T> getSingleCrudMapper();

    BatchCrudMapper<T> getBatchCrudMapper();

    SearchMapper<T> getSearchMapper();

    TagEditMapper getTagEditMapper();

}
