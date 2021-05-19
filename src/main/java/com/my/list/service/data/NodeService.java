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
        type.normalize(node);
        mainData.setExcerpt(type.generateExcerpt(node));
        
        mainDataService.add(mainData);
        if (type.getHasExtraData()) {
            ExtraData extraData = node.getExtraData();
            if (extraData == null) throw new DataException("Input extraData is null.");
            extraData.setExtraId(mainData.getId());
            extraDataService.add(extraData);
        }
        if (type.getHasExtraList()) {
            List<ListItem> extraList = node.getExtraList();
            if (extraList != null) {
                save(mainData.getId(), extraList);
            } else {
                if (type.getExtraListRequired()) throw new DataException("Input extraList is null.");
            }
        }

        updateExcerpts(partService.getParents(node.getMainData().getId()).stream()
            .map(n -> n.getMainData().getId()).collect(Collectors.toList()));
    }
    public Node get(Long nodeId) {
        if (nodeId == null) throw new DataException("Input nodeId is null.");
        
        MainData mainData = mainDataService.get(nodeId);
        permissionChecker.check(mainData, false);
        
        Node node = new NodeDTO(mainData);
        Type type = typeConfig.getType(mainData);
        
        if (type.getHasExtraData()) node.setExtraData(extraDataService.get(mainData.getId(), type.getExtraDataClass()));
        if (type.getHasExtraList() && type.getExtraListRequired()) node.setExtraList(
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

        Type type = typeConfig.getType(mainData);
        type.normalize(node);
        
        if (isSimple) {
            mainDataService.update(mainData, true);
        } else {
            mainData.setExcerpt(type.generateExcerpt(node));
            
            mainDataService.update(mainData, false);
            
            if (type.getHasExtraData()) {
                ExtraData extraData = node.getExtraData();
                if (extraData == null) throw new DataException("Input extraData is null.");
                extraData.setExtraId(mainData.getId());
                extraDataService.update(extraData);
            }
            if (type.getHasExtraList()) {
                List<ListItem> extraList = node.getExtraList();
                if (extraList != null) {
                    save(mainData.getId(), node.getExtraList());
                } else {
                    if (type.getExtraListRequired()) throw new DataException("Input extraList is null.");
                }
            }

            updateExcerpts(partService.getParents(node.getMainData().getId()).stream()
                .map(n -> n.getMainData().getId()).collect(Collectors.toList()));
        }
    }
    public void remove(Long nodeId) {
        if (nodeId == null) throw new DataException("Input nodeId is null.");

        List<Long> parentIds = partService.getParents(nodeId).stream()
            .map(n -> n.getMainData().getId()).collect(Collectors.toList());
        
        MainData mainData = mainDataService.get(nodeId);
        permissionChecker.check(mainData, true);
        Type type = typeConfig.getType(mainData);
        
        if (type.getHasExtraList()) partService.removeAllChildren(nodeId);
        mainDataService.remove(nodeId);

        updateExcerpts(parentIds);
    }

    public void updateExcerpts(List<Long> nodeIds) {
        for (Long nodeId : nodeIds) {
            Node node = get(nodeId);
            Type type = typeConfig.getType(node.getMainData().getType());
            String excerpt = type.getExcerptGenerator().generate(node);
            node.getMainData().setExcerpt(excerpt);
            update(node, true);
        }
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