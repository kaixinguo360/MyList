package com.my.list.type.text

import com.my.list.type.ValueMap

class Text(
    var id: Long? = null,
    var nodeId: Long? = null,
    var content: String? = null
): ValueMap {
    override fun toString(): String {
        return "Text[$id,$nodeId,$content]"
    }
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
