package com.my.list.module.common.controller;

import com.my.list.module.common.Resource;

public interface ResourceController<T extends Resource> extends SingleCrudController<T>, SearchController<T> {
}
