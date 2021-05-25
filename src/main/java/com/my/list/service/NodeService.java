package com.my.list.service;

import com.my.list.entity.*;
import com.my.list.exception.DataException;
import com.my.list.service.data.ExtraDataService;
import com.my.list.service.data.MainDataService;
import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public class NodeService {

    private final PermissionChecker permissionChecker;

    private final TypeManager typeManager;
    private final MainDataService mainDataService;
    private final ExtraDataService extraDataService;
    private final LinkService linkService;

    public NodeService(PermissionChecker permissionChecker, TypeManager typeManager, MainDataService mainDataService,
                       ExtraDataService extraDataService, LinkService linkService) {
        this.permissionChecker = permissionChecker;
        this.typeManager = typeManager;
        this.mainDataService = mainDataService;
        this.extraDataService = extraDataService;
        this.linkService = linkService;
    }

    public void add(Node node) {
        if (node == null) throw new DataException("Input node is null.");

        MainData mainData = node.getMainData();
        if (mainData == null) throw new DataException("Input mainData is null.");
        mainData.setUser(permissionChecker.getUserId());

        TypeDefinition typeDefinition = typeManager.getType(mainData);
        typeDefinition.normalize(node);
        mainData.setExcerpt(typeDefinition.generateExcerpt(node));

        mainDataService.add(mainData);
        if (typeDefinition.getHasExtraData()) {
            ExtraData extraData = node.getExtraData();
            if (extraData == null) throw new DataException("Input extraData is null.");
            extraData.setId(mainData.getId());
            extraDataService.add(extraData);
        }
        if (typeDefinition.getHasExtraList()) {
            List<ListItem> extraList = node.getExtraList();
            if (extraList != null) {
                saveExtraList(mainData.getId(), extraList);
            } else {
                if (typeDefinition.getExtraListRequired()) throw new DataException("Input extraList is null.");
            }
        }

        updateExcerpts(linkService.getParents(node.getMainData().getId()).stream()
            .map(n -> n.getMainData().getId()).collect(Collectors.toList()));
    }
    public Node get(Long nodeId) {
        if (nodeId == null) throw new DataException("Input nodeId is null.");

        MainData mainData = mainDataService.get(nodeId);
        permissionChecker.check(mainData, false);

        Node node = new NodeImpl(mainData);
        TypeDefinition typeDefinition = typeManager.getType(mainData);

        if (typeDefinition.getHasExtraData()) node.setExtraData(extraDataService.get(mainData.getId(), typeDefinition.getExtraDataClass()));
        if (typeDefinition.getHasExtraList() && typeDefinition.getExtraListRequired()) node.setExtraList(
            linkService.getChildren(nodeId)
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

        TypeDefinition typeDefinition = typeManager.getType(mainData);
        typeDefinition.normalize(node);

        if (isSimple) {
            mainDataService.update(mainData, true);
        } else {
            mainData.setExcerpt(typeDefinition.generateExcerpt(node));

            mainDataService.update(mainData, false);

            if (typeDefinition.getHasExtraData()) {
                ExtraData extraData = node.getExtraData();
                if (extraData == null) throw new DataException("Input extraData is null.");
                extraData.setId(mainData.getId());
                extraDataService.update(extraData);
            }
            if (typeDefinition.getHasExtraList()) {
                List<ListItem> extraList = node.getExtraList();
                if (extraList != null) {
                    saveExtraList(mainData.getId(), node.getExtraList());
                } else {
                    if (typeDefinition.getExtraListRequired()) throw new DataException("Input extraList is null.");
                }
            }

            updateExcerpts(linkService.getParents(node.getMainData().getId()).stream()
                .map(n -> n.getMainData().getId()).collect(Collectors.toList()));
        }
    }
    public void remove(Long nodeId) {
        if (nodeId == null) throw new DataException("Input nodeId is null.");

        List<Long> parentIds = linkService.getParents(nodeId).stream()
            .map(n -> n.getMainData().getId()).collect(Collectors.toList());

        MainData mainData = mainDataService.get(nodeId);
        permissionChecker.check(mainData, true);
        TypeDefinition typeDefinition = typeManager.getType(mainData);

        if (typeDefinition.getHasExtraList()) linkService.removeAllChildren(nodeId);
        mainDataService.remove(nodeId);

        updateExcerpts(parentIds);
    }

    public void updateExcerpts(List<Long> nodeIds) {
        for (Long nodeId : nodeIds) {
            Node node = get(nodeId);
            TypeDefinition typeDefinition = typeManager.getType(node.getMainData().getType());
            String excerpt = typeDefinition.getExcerptGenerator().generate(node);
            node.getMainData().setExcerpt(excerpt);
            update(node, true);
        }
    }

    private void saveExtraList(Long listId, List<ListItem> extraList) {
        if (listId == null) throw new DataException("Input listId is null");
        if (extraList == null) throw new DataException("Input extraList is null.");

        List<Long> childIds = extraList.stream().map(item -> {
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
        linkService.setChildren(listId, childIds);
    }

    // ---- Factory ---- //
    @Service
    public static class NodeServiceFactory {

        private final TypeManager typeManager;
        private final MainDataService mainDataService;
        private final ExtraDataService extraDataService;

        public NodeServiceFactory(TypeManager typeManager, MainDataService mainDataService, ExtraDataService extraDataService) {
            this.typeManager = typeManager;
            this.mainDataService = mainDataService;
            this.extraDataService = extraDataService;
        }

        public NodeService create(PermissionChecker permissionChecker, LinkService linkService) {
            return new NodeService(permissionChecker, typeManager, mainDataService, extraDataService, linkService);
        }
    }

}
