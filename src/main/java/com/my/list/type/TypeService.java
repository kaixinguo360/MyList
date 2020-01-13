package com.my.list.type;

import com.my.list.dto.Node;

public interface TypeService {
    
    void addNode(Node node);
    void removeNode(Long id);
    Node getNode(Long id);
    void updateNode(Node node);
    
}
