package com.my.list.service;

import com.my.list.domain.MainData;
import com.my.list.domain.Node;
import com.my.list.domain.NodeMapper;
import org.springframework.stereotype.Service;

@Service
public class MainDataService {

    private final NodeMapper nodeMapper;
    
    MainDataService(NodeMapper nodeMapper) {
        this.nodeMapper = nodeMapper;
    }
    
    void add(MainData mainData) {
        Node node = Node.Companion.fromSingleNode(mainData);
        if (node == null) throw new DataException("Input mainData is null.");
        if (mainData.getId() != null) throw new DataException("Id of input mainData has already set.");
        
        nodeMapper.insert(node);
        mainData.setId(node.getId());
    }
    MainData get(Long mainDataId) {
        if (mainDataId == null) throw new DataException("Input mainDataId is null.");

        MainData mainData = nodeMapper.selectByPrimaryKey(mainDataId);
        if (mainData == null) throw new DataException("Can't find mainData for node with id=" + mainDataId);

        return mainData;
    }
    void update(MainData mainData) {
        if (mainData == null) throw new DataException("Input mainData is null.");
        if (mainData.getId() == null) throw new DataException("Id of input mainData is not set.");
        
        nodeMapper.updateByPrimaryKey(Node.Companion.fromSingleNode(mainData));
    }
    void remove(Long mainDataId) {
        if (mainDataId == null) throw new DataException("Input mainDataId is null.");
        
        nodeMapper.deleteByPrimaryKey(mainDataId);
    }
}
