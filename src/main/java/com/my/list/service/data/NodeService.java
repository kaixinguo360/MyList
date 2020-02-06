package com.my.list.service.data;

import com.my.list.domain.ExtraData;
import com.my.list.domain.MainData;
import com.my.list.dto.*;
import com.my.list.exception.DataException;
import com.my.list.service.PermissionChecker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public class NodeService {
    
    private final PermissionChecker permissionChecker;

    private final TypeConfig typeConfig;
    private final MainDataService mainDataService;
    private final ExtraDataService extraDataService;
    private final PartService partService;

    public NodeService(PermissionChecker permissionChecker, TypeConfig typeConfig, MainDataService mainDataService,
                       ExtraDataService extraDataService, PartService partService) {
        this.permissionChecker = permissionChecker;
        this.typeConfig = typeConfig;
        this.mainDataService = mainDataService;
        this.extraDataService = extraDataService;
        this.partService = partService;
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
        if (type.isHasExtraList()) {
            List<ListItem> extraList = node.getExtraList();
            if (extraList != null) {
                save(mainData.getId(), extraList);
            } else {
                if (type.isExtraListRequired()) throw new DataException("Input extraList is null.");
            }
        }
    }
    public Node get(Long nodeId) {
        if (nodeId == null) throw new DataException("Input nodeId is null.");
        
        MainData mainData = mainDataService.get(nodeId);
        permissionChecker.check(mainData, false);
        
        Node node = new NodeDTO(mainData);
        Type type = typeConfig.getType(mainData);
        
        if (type.isHasExtraData()) node.setExtraData(extraDataService.get(mainData.getId(), type.getExtraDataClass()));
        if (type.isHasExtraList() && type.isExtraListRequired()) node.setExtraList(
            partService.getChildren(nodeId)
                .stream()
                .map(n -> new ListItem(n, ListItem.ItemStatus.EXIST))
                .collect(Collectors.toList())
        );
        return node;
    }
    public void update(Node node, boolean isSimple) {
        if (node == null) throw new DataException("Input node is null.");
        
        MainData mainData = node.getMainData();
        if (mainData == null) throw new DataException("Input mainData is null.");
        if (mainData.getUser() == null) throw new DataException("UserId of input mainData is not set.");
        
        MainData old = mainDataService.get(mainData.getId());
        permissionChecker.checkUpdate(old, mainData);
        
        if (isSimple) {
            mainDataService.update(mainData, true);
        } else {
            Type type = typeConfig.getType(mainData);
            type.process(node);
            
            mainDataService.update(mainData, false);
            
            if (type.isHasExtraData()) {
                ExtraData extraData = node.getExtraData();
                if (extraData == null) throw new DataException("Input extraData is null.");
                extraData.setExtraId(mainData.getId());
                extraDataService.update(extraData);
            }
            if (type.isHasExtraList()) {
                List<ListItem> extraList = node.getExtraList();
                if (extraList != null) {
                    save(mainData.getId(), node.getExtraList());
                } else {
                    if (type.isExtraListRequired()) throw new DataException("Input extraList is null.");
                }
            }
        }
    }
    public void remove(Long nodeId) {
        if (nodeId == null) throw new DataException("Input nodeId is null.");
        
        MainData mainData = mainDataService.get(nodeId);
        permissionChecker.check(mainData, true);
        Type type = typeConfig.getType(mainData);
        
        if (type.isHasExtraList()) partService.removeAllChildren(nodeId);
        mainDataService.remove(nodeId);
    }
    
    private void save(Long listId, List<ListItem> extraList) {
        if (listId == null) throw new DataException("Input listId is null");
        if (extraList == null) throw new DataException("Input extraList is null.");

        List<Long> partIds = extraList.stream().map(item -> {Long partId = null;
            switch (item.status) {
                case NEW:
                    Node newNode = item.node;
                    add(newNode);
                    return newNode.getMainData().getId();
                case UPDATE:
                    Node node = item.node;
                    update(node, false);
                    return node.getMainData().getId();
                case EXIST:
                default:
                    return item.node.getMainData().getId();
            }
        }).collect(Collectors.toList());
        partService.setChildren(listId, partIds);
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
        
        public NodeService create(PermissionChecker permissionChecker, PartService partService) {
            return new NodeService(permissionChecker, typeConfig, mainDataService, extraDataService, partService);
        }
    }
    
}
