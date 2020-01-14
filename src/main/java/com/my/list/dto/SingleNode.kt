package com.my.list.dto

import java.sql.Timestamp

interface SingleNode {
    var id: Long?
    var user: Long?
    var type: String?
    var ctime: Timestamp?
    var mtime: Timestamp?
    var title: String?
    var excerpt: String?
    var lstatus: String?
    var lcount: Int?
    var permissions: String?
    var nsfw: Boolean?
    var like: Boolean?
    var sourceUrl: String?
    var comment: String?
}
