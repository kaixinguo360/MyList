package com.my.list.service;

import com.my.list.domain.NodeMapper;
import com.my.list.dto.Node;
import org.springframework.stereotype.Component;

@Component
public class SingleNodeService implements NodeService {

    protected final NodeMapper nodeMapper;

    public SingleNodeService(NodeMapper nodeMapper) {
        this.nodeMapper = nodeMapper;
    }

    @Override
    public void addNode(Node node) {
        nodeMapper.insert(node.getDomain());
    }

    @Override
    public Node getNode(Long id) {
        if (id == null) throw new DataException("Id is null");
        com.my.list.domain.Node node = nodeMapper.selectByPrimaryKey(id);
        if (node == null) return null;
        return new Node(node);
    }

    @Override
    public void updateNode(Node node) {
        nodeMapper.updateByPrimaryKey(node.getDomain());
    }

    @Override
    public void removeNode(Long id) {
        nodeMapper.deleteByPrimaryKey(id);
    }
}
