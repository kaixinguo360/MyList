package com.my.list.type.text

import com.fasterxml.jackson.annotation.JsonTypeName
import com.my.list.domain.ExtraData

@JsonTypeName(value = "text")
class Text(
    var id: Long? = null,
    var content: String? = null
): ExtraData {
    override fun toString(): String {
        return "Text[$id,$content]"
    }
    override fun getExtraId(): Long? { return id; }
    override fun setExtraId(id: Long?) { this.id = id; }
    override fun toMap(): Map<String, Any?> {
        val map = HashMap<String, Any?>()
        map["text_id"] = id
        map["text_content"] = content
        return map
    }
}
