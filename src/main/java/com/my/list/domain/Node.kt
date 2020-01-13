package com.my.list.domain

import java.sql.Timestamp

class Node(
    var id: Long? = null,
    var user: Long? = null,
    var type: String? = "list",
    var ctime: Timestamp? = Timestamp(System.currentTimeMillis()),
    var mtime: Timestamp? = Timestamp(System.currentTimeMillis()),
    var title: String? = null,
    var excerpt: String? = null,
    var lstatus: String? = "alone",
    var lcount: Int? = 0,
    var permissions: String? = "private",
    var nsfw: Boolean? = false,
    var like: Boolean? = false,
    var sourceUrl: String? = null,
    var comment: String? = null
) {
    override fun toString(): String {
        return "Node[$id,$user,$type,$ctime,$mtime,$title,$excerpt,$lstatus,$lcount,$permissions,$nsfw,$like,$sourceUrl,$comment]"
    }
}
