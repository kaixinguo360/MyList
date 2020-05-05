package com.my.list.module.image;

import com.my.list.exception.NotImplementedException;
import com.my.list.module.common.mapper.BatchCrudMapper;
import com.my.list.module.common.mapper.SearchMapper;
import com.my.list.module.common.mapper.SingleCrudMapper;
import com.my.list.module.common.mapper.TagEditMapper;
import com.my.list.module.common.service.SearchService;
import com.my.list.module.common.service.SingleCurdService;
import com.my.list.module.common.service.TagEditService;
import com.my.list.system.mapper.User;
import com.my.list.system.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService implements SingleCurdService<Image>, SearchService<Image>, TagEditService<Image> {

    @Autowired private TagService tagService;
    @Autowired private ImageMapper mapper;

    @Override
    public TagService getTagService() {
        return tagService;
    }

    @Override
    public SingleCrudMapper<Image> getSingleCrudMapper() {
        return mapper;
    }

    @Override
    public BatchCrudMapper<Image> getBatchCrudMapper() {
        throw new NotImplementedException("Image_BatchCrudMapper");
    }

    @Override
    public SearchMapper<Image> getSearchMapper() {
        return mapper;
    }

    @Override
    public TagEditMapper getTagEditMapper() {
        return mapper;
    }

    @Override
    public List<Image> search(
        User user,
        List<String> andTags,
        List<String> orTags,
        List<String> notTags,
        List<String> includeText,
        List<String> excludeText,
        Integer limit,
        Integer offset,
        String orderBy,
        String orderDirection
    ) {
        switch (orderBy) {
            case "ctime":
            case "mtime":
                break;
            case "pageTitle":
                orderBy = "page_title";
                break;
            case "imageTitle":
                orderBy = "image_title";
                break;
            default:
                throw new NotImplementedException("Image_OrderBy[" + orderBy + "]");
        }
        return getSearchMapper().search(user, andTags, orTags, notTags, includeText, excludeText, limit, offset, orderBy, orderDirection);
    }
}
