package com.my.list.type.music

import com.fasterxml.jackson.annotation.JsonTypeName
import com.my.list.domain.ExtraData

@JsonTypeName(value = "music")
class Music(
    var id: Long? = null,
    var url: String? = null,
    var format: String? = null
): ExtraData {
    companion object { const val TYPE_NAME = "music" }
    override fun toString(): String {
        return "Music[$id,$url,$format]"
    }
    override fun getExtraId(): Long? { return id; }
    override fun setExtraId(id: Long?) { this.id = id; }
    override fun toMap(): Map<String, Any?> {
        val map = HashMap<String, Any?>()
        map["music_id"] = id
        map["music_url"] = url
        map["music_format"] = format
        return map
    }
}
