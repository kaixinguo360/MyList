package com.my.list.type.text;

import com.my.list.service.ExtraNodeService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TextService extends ExtraNodeService {

    public TextService(TextMapper textMapper) {
        super("text", textMapper);
    }
    
    public Text parse(Map<String, Object> map) {
        Text model = new Text();
        model.fromMap(map);
        return model;
    }
}
