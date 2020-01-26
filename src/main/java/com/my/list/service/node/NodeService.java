package com.my.list.service.node;

import com.my.list.domain.ExtraData;
import com.my.list.domain.MainData;
import com.my.list.dto.*;
import com.my.list.service.DataException;
import com.my.list.service.PermissionChecker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public class NodeService {
    
    private final PermissionChecker permissionChecker;

    private final TypeConfig typeConfig;
    private final MainDataService mainDataService;
    private final ExtraDataService extraDataService;
    private final ListService listService;

    public NodeService(PermissionChecker permissionChecker, TypeConfig typeConfig, MainDataService mainDataService,
                       ExtraDataService extraDataService, ListService listService) {
        this.permissionChecker = permissionChecker;
        this.typeConfig = typeConfig;
        this.mainDataService = mainDataService;
        this.extraDataService = extraDataService;
        this.listService = listService;
    }

    public void add(Node node) {
        if (node == null) throw new DataException("Input node is null.");
        
        MainData mainData = node.getMainData();
        if (mainData == null) throw new DataException("Input mainData is null.");
        mainData.setUser(permissionChecker.getUserId());
        Type type = typeConfig.getType(mainData);
        type.process(node);
        
        mainDataService.add(mainData);
        if (type.isHasExtraData()) {
            ExtraData extraData = node.getExtraData();
            if (extraData == null) throw new DataException("Input extraData is null.");
            extraData.setExtraId(mainData.getId());
            extraDataService.add(extraData);
        }
        if (type.isHasExtraList()) save(mainData.getId(), node.getExtraList());
    }
    public Node get(Long nodeId) {
        if (nodeId == null) throw new DataException("Input nodeId is null.");
        
        MainData mainData = mainDataService.get(nodeId);
        permissionChecker.check(mainData, true);
        
        Node node = new NodeDTO(mainData);
        Type type = typeConfig.getType(mainData);
        
        if (type.isHasExtraData()) node.setExtraData(extraDataService.get(mainData.getId(), type.getExtraDataClass()));
        if (type.isHasExtraList()) node.setExtraList(
            listService.getList(nodeId)
                .stream()
                .map(n -> new ListItem(n, ListItem.ItemStatus.EXIST))
                .collect(Collectors.toList())
        );
        return node;
    }
    public void update(Node node) {
        if (node == null) throw new DataException("Input node is null.");
        
        MainData mainData = node.getMainData();
        permissionChecker.check(mainData, false);
        Type type = typeConfig.getType(mainData);
        type.process(node);
        
        mainDataService.update(mainData);
        if (type.isHasExtraData()) {
            ExtraData extraData = node.getExtraData();
            if (extraData == null) throw new DataException("Input extraData is null.");
            extraData.setExtraId(mainData.getId());
            extraDataService.update(extraData);
        }
        if (type.isHasExtraList()) save(mainData.getId(), node.getExtraList());
    }
    public void remove(Long nodeId) {
        if (nodeId == null) throw new DataException("Input nodeId is null.");
        
        MainData mainData = mainDataService.get(nodeId);
        permissionChecker.check(mainData, false);
        Type type = typeConfig.getType(mainData);
        
        if (type.isHasExtraList())
            listService.removeList(nodeId);
        else
            mainDataService.remove(nodeId);
    }
    
    private void save(Long listId, List<ListItem> list) {
        if (listId == null) throw new DataException("Input listId is null");
        if (list == null) throw new DataException("Input list is null.");

        List<Long> partIds = list.stream().map(item -> {Long partId = null;
            switch (item.itemStatus) {
                case NEW:
                    Node newNode = item.node;
                    add(newNode);
                    return newNode.getMainData().getId();
                case UPDATE:
                    Node node = item.node;
                    update(node);
                    return node.getMainData().getId();
                case EXIST:
                default:
                    return item.node.getMainData().getId();
            }
        }).collect(Collectors.toList());
        listService.updateList(listId, partIds);
    }

    // ---- Factory ---- //
    @Service
    public static class NodeServiceFactory {
        
        private final TypeConfig typeConfig;
        private final MainDataService mainDataService;
        private final ExtraDataService extraDataService;

        public NodeServiceFactory(TypeConfig typeConfig, MainDataService mainDataService, ExtraDataService extraDataService) {
            this.typeConfig = typeConfig;
            this.mainDataService = mainDataService;
            this.extraDataService = extraDataService;
        }
        
        public NodeService create(PermissionChecker permissionChecker, ListService listService) {
            return new NodeService(permissionChecker, typeConfig, mainDataService, extraDataService, listService);
        }
    }
    
}
