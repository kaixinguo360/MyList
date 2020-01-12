package com.my.list.bean

class Video(
    var id: Long? = null,
    var node_id: Long? = null,
    var url: String? = null,
    var format: String? = null
) {
    override fun toString(): String {
        return "Text[$id,$node_id,$url,$format]"
    }
}
