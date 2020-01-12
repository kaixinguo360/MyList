package com.my.list.data.node

class Image(
    var id: Long? = null,
    var node_id: Long? = null,
    var url: String? = null,
    var description: String? = null
) {
    override fun toString(): String {
        return "Text[$id,$node_id,$url,$description]"
    }
}
