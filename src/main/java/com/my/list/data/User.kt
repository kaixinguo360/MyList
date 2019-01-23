package com.my.list.data

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*

@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Int = 0,

        @Column(unique = true, nullable = false)
        var name: String = "",

        @JsonIgnore
        @Column(nullable = false)
        var password: String = "",

        @JsonIgnore
        @Temporal(TemporalType.TIMESTAMP)
        @CreatedDate
        @Column(nullable = false, updatable = false)
        var createdTime: Date = Date(),

        @JsonIgnore
        @Temporal(TemporalType.TIMESTAMP)
        @LastModifiedDate
        @Column(nullable = false)
        var updatedTime: Date = Date()
) {}