package com.my.list.bean

class Image(
    var id: Long? = null,
    var nodeId: Long? = null,
    var url: String? = null,
    var description: String? = null
) {
    override fun toString(): String {
        return "Image[$id,$nodeId,$url,$description]"
    }
}
