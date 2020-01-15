package com.my.list.service;

import com.my.list.domain.NodeMapper;
import com.my.list.domain.ProcedureMapper;
import com.my.list.domain.User;
import com.my.list.dto.ExtraNode;
import com.my.list.dto.ListItem;
import com.my.list.dto.NodeDTO;
import com.my.list.dto.SingleNode;
import com.my.list.type.ExtraData;

import java.util.List;
import java.util.stream.Collectors;

public class ExtraNodeService {
    
    private final Long userId;

    private final TypeConfig typeConfig;
    private final SingleNodeService singleNodeService;

    private final NodeMapper nodeMapper;
    private final ProcedureMapper procedureMapper;
    
    public ExtraNodeService(User user, TypeConfig typeConfig, SingleNodeService singleNodeService,
                            NodeMapper nodeMapper, ProcedureMapper procedureMapper) {
        if (user == null) throw new DataException("User is null");
        if (user.getId() == null) throw new DataException("Id of user is null");
        this.userId = user.getId();
        this.typeConfig = typeConfig;
        this.singleNodeService = singleNodeService;
        this.nodeMapper = nodeMapper;
        this.procedureMapper = procedureMapper;
    }

    public void add(ExtraNode extraNode) {
        SingleNode singleNode = extraNode.getSingleNode();
        Type type = typeConfig.getType(singleNode.getType());
        singleNodeService.add(singleNode);
        if (type.hasExtraData) {
            ExtraData extraData = ExtraData.parse(type.extraDataClass, extraNode.getExtraData());
            extraData.setParentId(singleNode.getId());
            type.extraDataMapper.insert(extraData);
            extraNode.setExtraData(extraData.toMap());
        }
        if (type.hasExtraList) {
            saveList(singleNode.getId(), extraNode.getExtraList());
        }
    }
    public ExtraNode get(Long id) {
        SingleNode singleNode = singleNodeService.get(id);
        if (singleNode == null) return null;
        ExtraNode extraNode = new NodeDTO(singleNode);
        Type type = typeConfig.getType(singleNode.getType());
        if (type.hasExtraData) {
            ExtraData extraData = type.extraDataMapper.selectByNodeId(singleNode.getId());
            if (extraData == null) throw new DataException("Can't find extra data for node with id=" + id);
            extraNode.setExtraData(extraData.toMap());
        }
        if (type.hasExtraList) {
            List<ListItem> list = getList(id);
            extraNode.setExtraList(list);
        }
        return extraNode;
    }
    public void update(ExtraNode extraNode) {
        SingleNode singleNode = extraNode.getSingleNode();
        Type type = typeConfig.getType(singleNode.getType());
        singleNodeService.update(singleNode);
        if (type.hasExtraData) {
            ExtraData extraData = ExtraData.parse(type.extraDataClass, extraNode.getExtraData());
            if (extraData.getExtraId() == null) throw new DataException("Id of extra data is null");
            if (type.extraDataMapper.selectByPrimaryKey(extraData.getExtraId()) == null) throw new DataException("Can't find extra data with id=" + extraData.getExtraId());
            type.extraDataMapper.updateByPrimaryKey(extraData);
        }
        if (type.hasExtraList) {
            saveList(singleNode.getId(), extraNode.getExtraList());
        }
    }
    public void remove(Long id) {
        SingleNode singleNode = singleNodeService.get(id);
        if (singleNode == null) throw new DataException("No such node, id=" + id);
        Type type = typeConfig.getType(singleNode.getType());
        if (type.hasExtraList) {
            procedureMapper.delete_list(id);
        } else {
            singleNodeService.remove(id);
        }
    }

    // ---- List ---- //
    private void saveList(Long listId, List<ListItem> list) {
        if (listId == null) throw new DataException("List id is null");
        if (list == null) throw new DataException("Input list is null, listId=" + listId);
        procedureMapper.clean_list(listId);
        list.forEach(item -> {
            Long partId = null;
            switch (item.itemStatus) {
                case NEW:
                    ExtraNode newNode = item.extraNode;
                    add(newNode);
                    partId = newNode.getSingleNode().getId();
                    break;
                case UPDATE:
                    ExtraNode extraNode = item.extraNode;
                    update(extraNode);
                    partId = extraNode.getSingleNode().getId();
                    break;
                case EXIST:
                    partId = item.singleNode.getId();
                    break;
            }
            procedureMapper.add_part(listId, partId);
        });
        procedureMapper.clean_nodes();
    }
    private List<ListItem> getList(Long listId) {
        if (listId == null) throw new DataException("List id is null");
        return nodeMapper.selectAllByListIdWithUserId(userId, listId)
            .stream().map(ListItem::new).collect(Collectors.toList());
    }
}
