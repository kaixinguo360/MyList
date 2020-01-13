package com.my.list.service;

import com.my.list.domain.NodeMapper;
import com.my.list.dto.Node;
import com.my.list.type.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NodeService implements TypeService {

    @Autowired private TypeServiceManager typeServiceManager;
    @Autowired private NodeMapper nodeMapper;

    @Override
    public void addNode(Node node) {
        typeServiceManager.getService(node.getType()).addNode(node);
    }

    @Override
    public void removeNode(Long id) {
        typeServiceManager.getService(getTypeById(id)).removeNode(id);
    }

    @Override
    public Node getNode(Long id) {
        return typeServiceManager.getService(getTypeById(id)).getNode(id);
    }

    @Override
    public void updateNode(Node node) {
        typeServiceManager.getService(node.getType()).updateNode(node);
    }
    
    public String getTypeById(Long id) {
        com.my.list.domain.Node node = nodeMapper.selectByPrimaryKey(id);
        if (node == null) throw new DataException("No such node, id=" + id);
        return node.getType();
    }
}
