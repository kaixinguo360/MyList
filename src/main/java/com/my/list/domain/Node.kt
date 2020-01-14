package com.my.list.domain

import com.my.list.dto.SingleNode
import java.sql.Timestamp

class Node(
    override var id: Long? = null,
    override var user: Long? = null,
    override var type: String? = "list",
    override var ctime: Timestamp? = Timestamp(System.currentTimeMillis()),
    override var mtime: Timestamp? = Timestamp(System.currentTimeMillis()),
    override var title: String? = null,
    override var excerpt: String? = null,
    override var lstatus: String? = "alone",
    override var lcount: Int? = 0,
    override var permissions: String? = "private",
    override var nsfw: Boolean? = false,
    override var like: Boolean? = false,
    override var sourceUrl: String? = null,
    override var comment: String? = null
): SingleNode {
    override fun toString(): String {
        return "Node[$id,$user,$type,$ctime,$mtime,$title,$excerpt,$lstatus,$lcount,$permissions,$nsfw,$like,$sourceUrl,$comment]"
    }
    companion object { 
        fun fromSingleNode(s: SingleNode?): Node? {
            if (s == null) return null
            if (s is Node) return s
            return Node(s.id, s.user, s.type, s.ctime, s.mtime, s.title, s.excerpt, s.lstatus, s.lcount, s.permissions, s.nsfw, s.like, s.sourceUrl, s.comment)
        }
    }
}
