package com.my.list.service;

import com.my.list.dto.Node;

public interface NodeService {
    
    void addNode(Node node);
    Node getNode(Long id);
    void updateNode(Node node);
    void removeNode(Long id);
    
}
