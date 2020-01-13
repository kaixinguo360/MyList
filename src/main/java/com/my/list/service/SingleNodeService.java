package com.my.list.service;

import com.my.list.domain.NodeMapper;
import com.my.list.domain.ProcedureMapper;
import com.my.list.dto.Node;
import com.my.list.type.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SingleNodeService implements TypeService {

    @Autowired protected ProcedureMapper procedureMapper;
    @Autowired protected NodeMapper nodeMapper;

    @Override
    public void addNode(Node node) {
        nodeMapper.insert(node.getDomain());
    }

    @Override
    public void removeNode(Long id) {
        nodeMapper.deleteByPrimaryKey(id);
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
}
