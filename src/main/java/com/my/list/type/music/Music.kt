package com.my.list.type.music

import com.my.list.type.ExtraData

class Music(
    var id: Long? = null,
    var nodeId: Long? = null,
    var url: String? = null,
    var format: String? = null
): ExtraData {
    companion object { const val TYPE_NAME = "music" }
    override fun toString(): String {
        return "Music[$id,$nodeId,$url,$format]"
    }
    override fun getExtraId(): Long? { return id; }
    override fun setExtraId(id: Long?) { this.id = id; }
    override fun getParentId(): Long? { return nodeId; }
    override fun setParentId(id: Long?) { this.nodeId = id; }
    override fun toMap(): Map<String, Any?> {
        val map = HashMap<String, Any?>()
        map["music_id"] = id
        map["music_node_id"] = nodeId
        map["music_url"] = url
        map["music_format"] = format
        return map
    }
    override fun fromMap(map: Map<String, Any?>) {
        id = map["music_id"] as Long?
        nodeId = map["music_node_id"] as Long?
        url = map["music_url"] as String?
        format = map["music_format"] as String?
    }
}
