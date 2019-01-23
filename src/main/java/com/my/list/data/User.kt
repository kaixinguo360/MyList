package com.my.list.data

import javax.persistence.*

@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Int = 0,
        @Column(unique = true)
        var name: String = "",
        var password: String = ""
) {}