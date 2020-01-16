package com.my.list.type.image

import com.my.list.domain.ExtraData

class Image(
    var id: Long? = null,
    var nodeId: Long? = null,
    var url: String? = null,
    var description: String? = null
): ExtraData {
    companion object { const val TYPE_NAME = "image" }
    override fun toString(): String {
        return "Image[$id,$nodeId,$url,$description]"
    }
    override fun getExtraId(): Long? { return id; }
    override fun setExtraId(id: Long?) { this.id = id; }
    override fun getParentId(): Long? { return nodeId; }
    override fun setParentId(id: Long?) { this.nodeId = id; }
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
