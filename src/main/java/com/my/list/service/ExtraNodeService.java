package com.my.list.service;

import com.my.list.dto.ExtraNode;
import com.my.list.dto.NodeDTO;
import com.my.list.dto.SingleNode;
import com.my.list.type.ExtraData;
import org.springframework.stereotype.Service;

@Service
public class ExtraNodeService {

    private final TypeConfig typeConfig;
    private final SingleNodeService singleNodeService;
    
    public ExtraNodeService(TypeConfig typeConfig, SingleNodeService singleNodeService) {
        this.typeConfig = typeConfig;
        this.singleNodeService = singleNodeService;
    }

    public void add(ExtraNode extraNode) {
        Type type = typeConfig.getType(extraNode.getSingleNode().getType());
        singleNodeService.add(extraNode.getSingleNode());
        if (type.hasExtraData()) {
            ExtraData extraData = ExtraData.parse(type.extraDataClass, extraNode.getExtraData());
            extraData.setParentId(extraNode.getSingleNode().getId());
            type.extraMapper.insert(extraData);
            extraNode.setExtraData(extraData.toMap());
        }
    }

    public ExtraNode get(Long id) {
        SingleNode singleNode = singleNodeService.get(id);
        if (singleNode == null) return null;
        ExtraNode extraNode = new NodeDTO(singleNode);
        Type type = typeConfig.getType(singleNode.getType());
        if (type.hasExtraData()) {
            ExtraData extraData = type.extraMapper.selectByNodeId(singleNode.getId());
            if (extraData == null) throw new DataException("Can't find extra data for node with id=" + id);
            extraNode.setExtraData(extraData.toMap());
        }
        return extraNode;
    }

    public void update(ExtraNode extraNode) {
        Type type = typeConfig.getType(extraNode.getSingleNode().getType());
        if (type.hasExtraData()) {
            ExtraData extraData = ExtraData.parse(type.extraDataClass, extraNode.getExtraData());
            if (extraData.getExtraId() == null) throw new DataException("Id of extra data is null");
            if (type.extraMapper.selectByPrimaryKey(extraData.getExtraId()) == null) throw new DataException("Can't find extra data with id=" + extraData.getExtraId());
            singleNodeService.update(extraNode.getSingleNode());
            type.extraMapper.updateByPrimaryKey(extraData);
        } else {
            singleNodeService.update(extraNode.getSingleNode());
        }
    }

    public void remove(Long id) {
        singleNodeService.remove(id);
    }
}
