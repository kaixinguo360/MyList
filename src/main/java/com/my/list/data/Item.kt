package com.my.list.data

import com.my.list.data.content.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

@Entity
data class Item(
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
    var info: String? = null,

    @Column(nullable = true)
    var url: String? = null,

    @Column(nullable = true)
    var img: String? = null,

/* ---------- * ---------- * ---------- * ---------- */

    @ManyToOne(fetch = FetchType.LAZY)
    var list: MyList? = null
) {
    @ManyToMany(
        cascade = [
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH
        ]
    )
    @JoinTable(name = "post_tag")
    var tags: Set<Tag> = HashSet()

/* ---------- * ---------- * ---------- * ---------- */

    @OneToMany(
        mappedBy="item",
        orphanRemoval = true,
        cascade = [ CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE ]
    )
    var texts: List<Text> = ArrayList()

    @OneToMany(
        mappedBy="item",
        orphanRemoval = true,
        cascade = [ CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE ]
    )
    var images: List<Image> = ArrayList()

    @OneToMany(
        mappedBy="item",
        orphanRemoval = true,
        cascade = [ CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE ]
    )
    var musics: List<Music> = ArrayList()

    @OneToMany(
        mappedBy="item",
        orphanRemoval = true,
        cascade = [ CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE ]
    )
    var videos: List<Video> = ArrayList()

    @OneToMany(
        mappedBy="item",
        orphanRemoval = true,
        cascade = [ CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE ]
    )
    var links: List<Link> = ArrayList()
}
