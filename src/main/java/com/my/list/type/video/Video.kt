package com.my.list.type.video

import com.my.list.type.ValueMap

class Video(
    var id: Long? = null,
    var nodeId: Long? = null,
    var url: String? = null,
    var format: String? = null
): ValueMap {
    override fun toString(): String {
        return "Video[$id,$nodeId,$url,$format]"
    }
    override fun toMap(): Map<String, Any?> {
        val map = HashMap<String, Any?>()
        map["video_id"] = id
        map["video_node_id"] = nodeId
        map["video_url"] = url
        map["video_format"] = format
        return map
    }
    override fun fromMap(map: Map<String, Any?>) {
        id = map["video_id"] as Long?
        nodeId = map["video_node_id"] as Long?
        url = map["video_url"] as String?
        format = map["video_format"] as String?
    }
}
