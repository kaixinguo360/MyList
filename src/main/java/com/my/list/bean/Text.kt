package com.my.list.bean

class Text(
    var id: Long? = null,
    var node_id: Long? = null,
    var content: String? = null
) {
    override fun toString(): String {
        return "Text[$id,$node_id,$content]"
    }
}
