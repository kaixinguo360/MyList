package com.my.list.type.text

import com.my.list.domain.ExtraData

class Text(
    var id: Long? = null,
    var nodeId: Long? = null,
    var content: String? = null
): ExtraData {
    companion object { const val TYPE_NAME = "text" }
    override fun toString(): String {
        return "Text[$id,$nodeId,$content]"
    }
    override fun getExtraId(): Long? { return id; }
    override fun setExtraId(id: Long?) { this.id = id; }
    override fun getParentId(): Long? { return nodeId; }
    override fun setParentId(id: Long?) { this.nodeId = id; }
    override fun toMap(): Map<String, Any?> {
        val map = HashMap<String, Any?>()
        map["text_id"] = id
        map["text_node_id"] = nodeId
        map["text_content"] = content
        return map
    }
    override fun fromMap(map: Map<String, Any?>) {
        id = map["text_id"] as Long?
        nodeId = map["text_node_id"] as Long?
        content = map["text_content"] as String?
    }
}
