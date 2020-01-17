package com.my.list.service.node;

import com.my.list.domain.ExtraData;
import com.my.list.domain.MainData;
import com.my.list.domain.User;
import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import com.my.list.service.AuthException;
import com.my.list.service.DataException;
import org.springframework.stereotype.Service;

public class NodeService {
    
    private final Long userId;

    private final TypeConfig typeConfig;
    private final MainDataService mainDataService;
    private final ExtraDataService extraDataService;
    private final ListDataService listDataService;

    public NodeService(User user, TypeConfig typeConfig, MainDataService mainDataService, 
                       ExtraDataService extraDataService, ListDataService listDataService) {
        if (user == null) throw new DataException("User is null");
        if (user.getId() == null) throw new DataException("Id of user is null");
        this.userId = user.getId();
        this.typeConfig = typeConfig;
        this.mainDataService = mainDataService;
        this.extraDataService = extraDataService;
        this.listDataService = listDataService;
    }

    public void add(Node node) {
        if (node == null) throw new DataException("Input node is null.");
        
        MainData mainData = node.getMainData();
        if (mainData == null) throw new DataException("Input mainData is null.");
        mainData.setUser(userId);
        Type type = typeConfig.getType(mainData);
        
        mainDataService.add(mainData);
        if (type.hasExtraData) {
            ExtraData extraData = node.getExtraData();
            if (extraData == null) throw new DataException("Input extraData is null.");
            extraData.setParentId(mainData.getId());
            extraDataService.add(node.getExtraData());
        }
        if (type.hasExtraList) listDataService.save(mainData.getId(), node.getExtraList(), this);
    }
    public Node get(Long nodeId) {
        if (nodeId == null) throw new DataException("Input nodeId is null.");
        
        MainData mainData = mainDataService.get(nodeId);
        checkPermission(mainData, true);
        
        Node node = new NodeDTO(mainData);
        Type type = typeConfig.getType(mainData);
        
        if (type.hasExtraData) node.setExtraData(extraDataService.get(mainData.getId(), type.extraDataClass));
        if (type.hasExtraList) node.setExtraList(listDataService.get(nodeId));
        return node;
    }
    public void update(Node node) {
        if (node == null) throw new DataException("Input node is null.");
        
        MainData mainData = node.getMainData();
        checkPermission(mainData, false);
        Type type = typeConfig.getType(mainData);
        
        mainDataService.update(mainData);
        if (type.hasExtraData) extraDataService.update(node.getExtraData());
        if (type.hasExtraList) listDataService.save(mainData.getId(), node.getExtraList(), this);
    }
    public void remove(Long nodeId) {
        if (nodeId == null) throw new DataException("Input nodeId is null.");
        
        MainData mainData = mainDataService.get(nodeId);
        checkPermission(mainData, false);
        Type type = typeConfig.getType(mainData);
        
        if (type.hasExtraList)
            listDataService.remove(nodeId);
        else
            mainDataService.remove(nodeId);
    }
    
    private void checkPermission(MainData mainData, boolean readOnly) {
        String permission = mainData.getPermission();
        if (permission == null) throw new AuthException("Permission is null");
        boolean success;
        switch (permission) {
            case "public" :
                success = true;
                break;
            case "protect" :
                success = readOnly || userId.equals(mainData.getUser());
                break;
            case "private" :
                success = userId.equals(mainData.getUser());
                break;
            default:
                throw new AuthException("Unknown permission: " + permission);
        }
        if (!success) throw new AuthException("Permission denied, permission=" + mainData.getPermission() +
            ", expectedUserId=" + mainData.getUser() + ", actualUserId=" + userId);
    }
    
    @Service
    public static class NodeServiceFactory {
        
        private final TypeConfig typeConfig;
        private final MainDataService mainDataService;
        private final ExtraDataService extraDataService;
        private final ListDataService listDataService;

        public NodeServiceFactory(TypeConfig typeConfig, MainDataService mainDataService, ExtraDataService extraDataService, ListDataService listDataService) {
            this.typeConfig = typeConfig;
            this.mainDataService = mainDataService;
            this.extraDataService = extraDataService;
            this.listDataService = listDataService;
        }
        
        public NodeService create(User user) {
            return new NodeService(user, typeConfig, mainDataService, extraDataService, listDataService);
        }
    }
    
}
