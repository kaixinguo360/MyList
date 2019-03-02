package com.my.list.data.content

import com.my.list.data.Item
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*

@Entity
data class Text(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    var item: Item,

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
    var title: String = "",

    @Column(nullable = true)
    var content: String? = null
) {}
