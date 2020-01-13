package com.my.list.type.image

import com.my.list.type.ValueMap

class Image(
    var id: Long? = null,
    var nodeId: Long? = null,
    var url: String? = null,
    var description: String? = null
): ValueMap {
    override fun toString(): String {
        return "Image[$id,$nodeId,$url,$description]"
    }
    override fun toMap(): Map<String, Any?> {
        val map = HashMap<String, Any?>()
        map["image_id"] = id
        map["image_node_id"] = nodeId
        map["image_url"] = url
        map["image_description"] = description
        return map
    }
    override fun fromMap(map: Map<String, Any?>) {
        id = map["image_id"] as Long?
        nodeId = map["image_node_id"] as Long?
        url = map["image_url"] as String?
        description = map["image_description"] as String?
    }
}
