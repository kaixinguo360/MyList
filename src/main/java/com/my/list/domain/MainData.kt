package com.my.list.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.sql.Timestamp

@JsonDeserialize(`as` = Node::class)
interface MainData {
    var id: Long?
    var user: Long?
    var type: String?
    var ctime: Timestamp?
    var mtime: Timestamp?
    var title: String?
    var excerpt: String?
    var linkForward: Int?
    var linkBack: Int?
    var linkDelete: Boolean?
    var linkVirtual: Boolean?
    var permission: String?
    var nsfw: Boolean?
    var like: Boolean?
    var hide: Boolean?
    var sourceUrl: String?
    var comment: String?
}
