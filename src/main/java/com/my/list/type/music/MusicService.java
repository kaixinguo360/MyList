package com.my.list.type.music;

import com.my.list.service.ExtraNodeService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MusicService extends ExtraNodeService {

    public MusicService(MusicMapper musicMapper) {
        super("music", musicMapper);
    }

    public Music parse(Map<String, Object> map) {
        Music model = new Music();
        model.fromMap(map);
        return model;
    }
}
