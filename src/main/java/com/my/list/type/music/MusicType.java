package com.my.list.type.music;

import com.my.list.domain.ExtraData;
import com.my.list.domain.ExtraDataMapper;
import com.my.list.dto.Type;
import org.springframework.stereotype.Component;

@Component
public class MusicType implements Type {
    
    private final MusicMapper musicMapper;

    public MusicType(MusicMapper musicMapper) {
        this.musicMapper = musicMapper;
    }

    public String getTypeName() {
        return Music.TYPE_NAME;
    }
    public Class<? extends ExtraData> getExtraDataClass() {
        return Music.class;
    }
    public ExtraDataMapper getExtraDataMapper() {
        return musicMapper;
    }
    public boolean isHasExtraData() {
        return true;
    }
    public boolean isHasExtraList() {
        return false;
    }
}
