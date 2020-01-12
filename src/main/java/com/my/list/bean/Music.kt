package com.my.list.bean

class Music(
    var id: Long? = null,
    var nodeId: Long? = null,
    var url: String? = null,
    var format: String? = null
) {
    override fun toString(): String {
        return "Music[$id,$nodeId,$url,$format]"
    }
}
