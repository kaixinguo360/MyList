package com.my.list.type.image

import com.fasterxml.jackson.annotation.JsonTypeName
import com.my.list.domain.ExtraData

@JsonTypeName(value = "image")
class Image(
    var id: Long? = null,
    var url: String? = null,
    var type: String? = null,
    var author: String? = null,
    var gallery: String? = null,
    var source: String? = null
): ExtraData {
    override fun toString(): String {
        return "Image[$id,$url,$type,$author,$gallery,$source]"
    }
    override fun getExtraId(): Long? { return id; }
    override fun setExtraId(id: Long?) { this.id = id; }
    override fun toMap(): Map<String, Any?> {
        val map = HashMap<String, Any?>()
        map["image_id"] = id
        map["image_url"] = url
        map["image_type"] = type
        map["image_author"] = author
        map["image_gallery"] = gallery
        map["image_source"] = source
        return map
    }
}
