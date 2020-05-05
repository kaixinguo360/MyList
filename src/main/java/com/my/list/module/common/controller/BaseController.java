package com.my.list.module.common.controller;

import com.my.list.module.common.Resource;
import com.my.list.module.common.service.BatchCrudService;
import com.my.list.module.common.service.SearchService;
import com.my.list.module.common.service.SingleCurdService;
import com.my.list.module.common.service.TagEditService;

public interface BaseController<T extends Resource> {

    SingleCurdService<T> getSingleCurdService();

    BatchCrudService<T> getBatchCrudService();

    SearchService<T> getSearchService();

    TagEditService<T> getTagEditService();

}
