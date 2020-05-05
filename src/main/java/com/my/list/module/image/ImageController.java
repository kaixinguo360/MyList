package com.my.list.module.image;

import com.my.list.exception.NotImplementedException;
import com.my.list.module.common.controller.CrudController;
import com.my.list.module.common.controller.SearchController;
import com.my.list.module.common.controller.TagEditController;
import com.my.list.module.common.service.BatchCrudService;
import com.my.list.module.common.service.SearchService;
import com.my.list.module.common.service.SingleCurdService;
import com.my.list.module.common.service.TagEditService;
import com.my.list.util.Authorization;
import com.my.list.util.SimpleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.my.list.Constants.API_ROOT;

@Authorization
@SimpleController
@RequestMapping(API_ROOT + "/image")
public class ImageController implements CrudController<Image>, SearchController<Image>, TagEditController<Image> {

    @Autowired private ImageService service;

    @Override
    public SingleCurdService<Image> getSingleCurdService() {
        return service;
    }

    @Override
    public BatchCrudService<Image> getBatchCrudService() {
        throw new NotImplementedException("Image_BatchCrudService");
    }

    @Override
    public SearchService<Image> getSearchService() {
        return service;
    }

    @Override
    public TagEditService<Image> getTagEditService() {
        return service;
    }
}
