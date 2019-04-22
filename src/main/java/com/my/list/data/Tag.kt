package com.my.list.data

import com.fasterxml.jackson.annotation.JsonIgnore
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

    @JsonIgnore
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

    @Column(nullable = false, length = 255)
    var title: String = "",

    @Column(nullable = true, length = 1023)
    var info: String? = null
) {
    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    var items: Set<Item> = HashSet()
}
