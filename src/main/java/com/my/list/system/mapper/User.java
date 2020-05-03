package com.my.list.system.mapper;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private String pass;
    private String email;
    private String status;
}
