package com.my.list.domain

class Part(
    var id: Long? = null,
    var parentId: Long? = null,
    var contentId: Long? = null,
    var contentOrder: Int? = null,
    var contentType: String? = null
) {
    override fun toString(): String {
        return "Text[$id,$parentId,$contentId,$contentOrder,$contentType]"
    }
}
