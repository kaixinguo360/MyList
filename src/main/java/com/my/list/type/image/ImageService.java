package com.my.list.type.image;

import com.my.list.service.ExtraNodeService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ImageService extends ExtraNodeService {

    public ImageService(ImageMapper imageMapper) {
        super("image", imageMapper);
    }

    public Image parse(Map<String, Object> map) {
        Image model = new Image();
        model.fromMap(map);
        return model;
    }
}
