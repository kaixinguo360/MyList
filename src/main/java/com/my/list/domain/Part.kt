package com.my.list.domain

class Part(
    var id: Long? = null,
    var parentId: Long? = null,
    var contentId: Long? = null,
    var contentOrder: Int? = null
) {
    override fun toString(): String {
        return "Text[$id,$parentId,$contentId,$contentOrder]"
    }
}
