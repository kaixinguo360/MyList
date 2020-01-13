package com.my.list;

import com.my.list.service.SingleNodeService;
import com.my.list.service.TypeServiceManager;
import com.my.list.type.text.TextService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class MyListApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyListApplication.class, args);
    }
    
    @Bean
    public TypeServiceManager typeServiceManager(
        SingleNodeService singleNodeService,
        TextService textService
    ) {
        TypeServiceManager typeServiceManager = new TypeServiceManager();
        typeServiceManager.putService("node", singleNodeService);
        typeServiceManager.putService("text", textService);
//        typeServiceManager.putService("image", imageService);
//        typeServiceManager.putService("music", musicService);
//        typeServiceManager.putService("video", videoService);
        return typeServiceManager;
    }

}
