package com.my.list.module.common.service;

import com.my.list.module.common.Resource;

public interface ResourceService<T extends Resource> extends SingleCurdService<T>, SearchService<T> {
}
