package com.my.list.type.video;

import com.my.list.service.ExtraNodeService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VideoService extends ExtraNodeService {

    public VideoService(VideoMapper videoMapper) {
        super("video", videoMapper);
    }

    public Video parse(Map<String, Object> map) {
        Video model = new Video();
        model.fromMap(map);
        return model;
    }
}
