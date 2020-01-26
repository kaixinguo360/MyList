package com.my.list.domain

import java.sql.Timestamp

class Node(
    override var id: Long? = null,
    override var user: Long? = null,
    override var type: String? = null,
    override var ctime: Timestamp? = Timestamp(System.currentTimeMillis()),
    override var mtime: Timestamp? = Timestamp(System.currentTimeMillis()),
    override var title: String? = null,
    override var excerpt: String? = null,
    override var linkDelete: Boolean? = null,
    override var linkVirtual: Boolean? = null,
    override var permission: String? = null,
    override var nsfw: Boolean? = null,
    override var like: Boolean? = null,
    override var hide: Boolean? = null,
    override var sourceUrl: String? = null,
    override var comment: String? = null
): MainData {
    override fun toString(): String {
        return "Node[$id,$user,$type,$ctime,$mtime,$title,$excerpt,$linkDelete,$linkVirtual,$permission,$nsfw,$like,$hide,$sourceUrl,$comment]"
    }
    companion object { 
        fun fromSingleNode(s: MainData?): Node? {
            if (s == null) return null
            if (s is Node) return s
            return Node(s.id, s.user, s.type, s.ctime, s.mtime, s.title, s.excerpt, s.linkDelete, s.linkVirtual, s.permission, s.nsfw, s.like, s.hide, s.sourceUrl, s.comment)
        }
        fun defaultNode(): Node {
            val node = Node()
            node.permission = "private"
            node.linkDelete = false
            node.linkVirtual = false
            node.nsfw = false
            node.like = false
            node.hide = false
            return node
        }
    }
}
