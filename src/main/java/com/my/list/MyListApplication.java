package com.my.list;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.my.list.mapper")
@SpringBootApplication
public class MyListApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyListApplication.class, args);
    }

}
