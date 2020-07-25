package com.my.list.modules.user;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private String pass;
    private String email;
    private String status;
}
