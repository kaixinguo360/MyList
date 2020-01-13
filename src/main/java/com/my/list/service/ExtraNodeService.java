package com.my.list.service;

import com.my.list.dto.Node;
import com.my.list.type.ExtraMapper;
import com.my.list.type.ExtraValues;

import java.util.Map;

public abstract class ExtraNodeService extends SingleNodeService {
    
    public String typeName;
    protected ExtraMapper extraMapper;
    
    protected ExtraNodeService(String typeName, ExtraMapper extraMapper) {
        this.typeName = typeName;
        this.extraMapper = extraMapper;
    }

    @Override
    public void addNode(Node node) {
        super.addNode(node);
        ExtraValues extraValues = parse(node.getExtraValues());
        extraValues.setParentId(node.getId());
        extraMapper.insert(extraValues);
        node.setExtraValues(extraValues.toMap());
    }

    @Override
    public Node getNode(Long id) {
        Node node = super.getNode(id);
        if (node == null) return null;
        if (!typeName.equals(node.getType()))
            throw new DataException("Type mismatch, expected=" + typeName + ", actual=" + node.getType());
        ExtraValues extraValues = extraMapper.selectByNodeId(node.getId());
        if (extraValues == null) throw new DataException("Can't find extra data for node with id=" + id);
        node.setExtraValues(extraValues.toMap());
        return node;
    }

    @Override
    public void updateNode(Node node) {
        ExtraValues extraValues = parse(node.getExtraValues());
        if (extraValues.getExtraId() == null) throw new DataException("Id of extra data is null");
        if (extraMapper.selectByPrimaryKey(extraValues.getExtraId()) == null) throw new DataException("Can't find extra data with id=" + extraValues.getExtraId());
        super.updateNode(node);
        extraMapper.updateByPrimaryKey(extraValues);
    }

    public abstract ExtraValues parse(Map<String, Object> map);
}
