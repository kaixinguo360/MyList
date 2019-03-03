package com.my.list.data.content

import com.fasterxml.jackson.annotation.JsonIgnore
import com.my.list.data.Item
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*

@Entity
data class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    var item: Item? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdTime: Date = Date(),

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(nullable = false)
    var updatedTime: Date = Date(),

/* ---------- * ---------- * ---------- * ---------- */

    @Column(nullable = false)
    var url: String = "",

    @Column(nullable = true)
    var info: String? = null
) {}
