package com.my.list.type.image

import com.fasterxml.jackson.annotation.JsonTypeName
import com.my.list.domain.ExtraData

@JsonTypeName(value = "image")
class Image(
    var id: Long? = null,
    var url: String? = null,
    var description: String? = null
): ExtraData {
    companion object { const val TYPE_NAME = "image" }
    override fun toString(): String {
        return "Image[$id,$url,$description]"
    }
    override fun getExtraId(): Long? { return id; }
    override fun setExtraId(id: Long?) { this.id = id; }
    override fun toMap(): Map<String, Any?> {
        val map = HashMap<String, Any?>()
        map["image_id"] = id
        map["image_url"] = url
        map["image_description"] = description
        return map
    }
}
