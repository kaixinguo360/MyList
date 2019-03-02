package com.my.list.data

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*
import kotlin.collections.HashSet

@Entity
data class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0,

    @Column(nullable = false)
    var userId: Int = 0,

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
    var info: String? = null
) {
    @ManyToMany(mappedBy = "tags")
    var items: Set<Item> = HashSet()
}
