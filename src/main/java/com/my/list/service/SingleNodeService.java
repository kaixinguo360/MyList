package com.my.list.service;

import com.my.list.domain.Node;
import com.my.list.domain.NodeMapper;
import com.my.list.dto.SingleNode;
import org.springframework.stereotype.Component;

@Component
public class SingleNodeService {

    protected final NodeMapper nodeMapper;

    public SingleNodeService(NodeMapper nodeMapper) {
        this.nodeMapper = nodeMapper;
    }

    public void add(SingleNode singleNode) {
        Node node = Node.Companion.fromSingleNode(singleNode);
        if (node == null) throw new DataException("Input node is null.");
        nodeMapper.insert(node);
        singleNode.setId(node.getId());
    }
    
    public SingleNode get(Long id) {
        if (id == null) throw new DataException("Id is null");
        return nodeMapper.selectByPrimaryKey(id);
    }
    
    public void update(SingleNode singleNode) {
        nodeMapper.updateByPrimaryKey(Node.Companion.fromSingleNode(singleNode));
    }
    
    public void remove(Long id) {
        nodeMapper.deleteByPrimaryKey(id);
    }
}
