package com.my.list.service;

import com.my.list.domain.Node;
import com.my.list.domain.NodeMapper;
import com.my.list.domain.User;
import com.my.list.dto.SingleNode;

public class SingleNodeService {

    private final Long userId;
    protected final NodeMapper nodeMapper;
    
    public SingleNodeService(User user, NodeMapper nodeMapper) {
        if (user == null) throw new DataException("User is null");
        if (user.getId() == null) throw new DataException("Id of user is null");
        this.userId = user.getId();
        this.nodeMapper = nodeMapper;
    }
    
    public void add(SingleNode singleNode) {
        Node node = Node.Companion.fromSingleNode(singleNode);
        nodeMapper.insertWithUserId(userId, node);
        if (node == null) throw new DataException("Input node is null.");
        singleNode.setId(node.getId());
        singleNode.setUser(userId);
    }
    public SingleNode get(Long id) {
        if (id == null) throw new DataException("Id is null");
        
        SingleNode singleNode = nodeMapper.selectByPrimaryKeyWithUserId(userId, id);
        
        if (singleNode == null || !userId.equals(singleNode.getUser())) return null;
        return singleNode;
    }
    public void update(SingleNode singleNode) {
        nodeMapper.updateByPrimaryKeyWithUserId(userId,Node.Companion.fromSingleNode(singleNode));
    }
    public void remove(Long id) {
        nodeMapper.deleteByPrimaryKeyWithUserId(userId, id);
    }
}
