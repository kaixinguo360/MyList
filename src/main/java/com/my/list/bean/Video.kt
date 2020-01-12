package com.my.list.bean

class Video(
    var id: Long? = null,
    var nodeId: Long? = null,
    var url: String? = null,
    var format: String? = null
) {
    override fun toString(): String {
        return "Video[$id,$nodeId,$url,$format]"
    }
}
