package com.my.list.service.data;

import com.my.list.domain.MainData;
import com.my.list.domain.Node;
import com.my.list.domain.NodeMapper;
import com.my.list.exception.DataException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
class MainDataService {

    private final NodeMapper nodeMapper;
    
    MainDataService(NodeMapper nodeMapper) {
        this.nodeMapper = nodeMapper;
    }
    
    void add(MainData mainData) {
        Node node = Node.Companion.fromSingleNode(mainData);
        if (node == null) throw new DataException("Input mainData is null.");
        if (mainData.getId() != null) throw new DataException("Id of input mainData has already set.");

        if (node.getCtime() == null) {
            node.setCtime(new Timestamp(System.currentTimeMillis()));
        }
        if (node.getMtime() == null) {
            node.setMtime(new Timestamp(System.currentTimeMillis()));
        }
        
        nodeMapper.insert(node);
        mainData.setId(node.getId());
    }
    MainData get(Long mainDataId) {
        if (mainDataId == null) throw new DataException("Input mainDataId is null.");

        MainData mainData = nodeMapper.select(mainDataId);
        if (mainData == null) throw new DataException("Can't find mainData for node with id=" + mainDataId);

        return mainData;
    }
    void update(MainData mainData, boolean isSimple) {
        if (mainData == null) throw new DataException("Input mainData is null.");
        if (mainData.getId() == null) throw new DataException("Id of input mainData is not set.");

        mainData.setMtime(new Timestamp(System.currentTimeMillis()));
        
        nodeMapper.update(Node.Companion.fromSingleNode(mainData), isSimple);
    }
    void remove(Long mainDataId) {
        if (mainDataId == null) throw new DataException("Input mainDataId is null.");
        
        nodeMapper.delete(mainDataId);
    }
}
