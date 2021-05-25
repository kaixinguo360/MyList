package com.my.list.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

    private Long id;
    private String name;
    private String pass;
    private String email;
    private String status;
}
