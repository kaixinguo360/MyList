package com.my.list.bean

class Text(
    var id: Long? = null,
    var nodeId: Long? = null,
    var content: String? = null
) {
    override fun toString(): String {
        return "Text[$id,$nodeId,$content]"
    }
}
