package com.my.list.domain;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String name;
    private String pass;
    private String email;
    private String status;

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", pass='" + pass + '\'' +
            ", email='" + email + '\'' +
            ", status='" + status + '\'' +
            '}';
    }
}
