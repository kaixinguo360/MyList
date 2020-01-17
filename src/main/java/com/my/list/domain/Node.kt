package com.my.list.domain

import java.sql.Timestamp

class Node(
    override var id: Long? = null,
    override var user: Long? = null,
    override var type: String? = "list",
    override var ctime: Timestamp? = Timestamp(System.currentTimeMillis()),
    override var mtime: Timestamp? = Timestamp(System.currentTimeMillis()),
    override var title: String? = null,
    override var excerpt: String? = null,
    override var linkForward: Int? = 0,
    override var linkBack: Int? = 0,
    override var linkDelete: Boolean? = false,
    override var linkVirtual: Boolean? = false,
    override var permission: String? = "private",
    override var nsfw: Boolean? = false,
    override var like: Boolean? = false,
    override var hide: Boolean? = false,
    override var sourceUrl: String? = null,
    override var comment: String? = null
): MainData {
    override fun toString(): String {
        return "Node[$id,$user,$type,$ctime,$mtime,$title,$excerpt,$linkForward,$linkBack,$linkDelete,$linkVirtual,$permission,$nsfw,$like,$hide,$sourceUrl,$comment]"
    }
    companion object { 
        fun fromSingleNode(s: MainData?): Node? {
            if (s == null) return null
            if (s is Node) return s
            return Node(s.id, s.user, s.type, s.ctime, s.mtime, s.title, s.excerpt, s.linkForward, s.linkBack, s.linkDelete, s.linkVirtual, s.permission, s.nsfw, s.like, s.hide, s.sourceUrl, s.comment)
        }
    }
}
