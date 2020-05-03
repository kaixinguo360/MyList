package com.my.list.module.image;

import com.my.list.module.common.BaseResourceService;
import org.springframework.stereotype.Service;

@Service
public class ImageService extends BaseResourceService<Image> {
    public ImageService(ImageMapper mapper) {
        super(mapper);
    }
}
