package com.my.list.type.video;

import com.my.list.domain.ExtraData;
import com.my.list.domain.ExtraDataMapper;
import com.my.list.dto.Type;
import org.springframework.stereotype.Component;

@Component
public class VideoType implements Type {
    
    private final VideoMapper videoMapper;

    public VideoType(VideoMapper videoMapper) {
        this.videoMapper = videoMapper;
    }

    public String getTypeName() {
        return Video.TYPE_NAME;
    }
    public Class<? extends ExtraData> getExtraDataClass() {
        return Video.class;
    }
    public ExtraDataMapper getExtraDataMapper() {
        return videoMapper;
    }
    public boolean isHasExtraData() {
        return true;
    }
    public boolean isHasExtraList() {
        return false;
    }
}
