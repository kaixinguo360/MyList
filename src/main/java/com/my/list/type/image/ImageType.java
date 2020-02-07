package com.my.list.type.image;

import com.my.list.domain.ExtraData;
import com.my.list.domain.ExtraDataMapper;
import com.my.list.dto.Type;
import org.springframework.stereotype.Component;

@Component
public class ImageType implements Type {
    
    private final ImageMapper imageMapper;

    public ImageType(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    public String getTypeName() {
        return Image.TYPE_NAME;
    }
    public Class<? extends ExtraData> getExtraDataClass() {
        return Image.class;
    }
    public ExtraDataMapper getExtraDataMapper() {
        return imageMapper;
    }
    public boolean isHasExtraData() {
        return true;
    }
    public boolean isHasExtraList() {
        return false;
    }
}
