package com.my.list.data.content

import com.fasterxml.jackson.annotation.JsonIgnore
import com.my.list.data.Item
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*

@Entity
data class Text(
    @Id
    @JsonIgnore
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

    @Column(nullable = false, length = 255)
    var title: String = "",

    @Column(nullable = true, length = 10230)
    var content: String? = null
) {}
