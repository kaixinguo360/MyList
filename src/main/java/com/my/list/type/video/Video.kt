package com.my.list.type.video

import com.fasterxml.jackson.annotation.JsonTypeName
import com.my.list.domain.ExtraData

@JsonTypeName(value = "video")
class Video(
    var id: Long? = null,
    var url: String? = null,
    var format: String? = null
): ExtraData {
    override fun toString(): String {
        return "Video[$id,$url,$format]"
    }
    override fun getExtraId(): Long? { return id; }
    override fun setExtraId(id: Long?) { this.id = id; }
    override fun toMap(): Map<String, Any?> {
        val map = HashMap<String, Any?>()
        map["video_id"] = id
        map["video_url"] = url
        map["video_format"] = format
        return map
    }
}
