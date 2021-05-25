package com.my.list.service;

import com.my.list.entity.MainData;
import com.my.list.entity.NodeImpl;
import com.my.list.exception.DataException;
import com.my.list.mapper.LinkMapper;
import com.my.list.mapper.MainDataMapper;
import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LinkService {

    private final PermissionChecker permissionChecker;

    private final TypeManager typeManager;
    private final MainDataMapper mainDataMapper;
    private final LinkMapper linkMapper;

    LinkService(PermissionChecker permissionChecker, TypeManager typeManager, MainDataMapper mainDataMapper, LinkMapper linkMapper) {
        this.permissionChecker = permissionChecker;
        this.typeManager = typeManager;
        this.mainDataMapper = mainDataMapper;
        this.linkMapper = linkMapper;
    }

    // ---- Add ---- //
    public void addChildren(List<Long> parentIds, List<Long> childIds) {
        for (Long parentId : parentIds) {
            addChildren(parentId, childIds);
        }
    }
    public void addChildren(Long parentId, List<Long> childIds) {
        MainData parentMainData = checkListPermission(parentId, true);
        TypeDefinition typeDefinition = typeManager.getType(parentMainData);
        
        if (typeDefinition.getExtraListUnique()) {
            Set<Long> ids = linkMapper.selectAllChildren(parentId)
                .stream().map(MainData::getId).collect(Collectors.toSet());
            childIds = (ids.size() == 0) ?
                childIds : childIds.stream().filter(childId -> !ids.contains(childId)).collect(Collectors.toList());
        }
        if (childIds.size() != 0) {
            linkMapper.insertChildren(parentId, childIds);
        }
    }
    public void addParents(List<Long> childIds, List<Long> parentIds) {
        for (Long childId : childIds) {
            addParents(childId, parentIds);
        }
    }
    public void addParents(Long childId, List<Long> parentIds) {
        checkNodePermission(childId, true);
        
        Set<Long> ids = linkMapper.selectAllParent(permissionChecker.getUserId(), childId)
                .stream().map(MainData::getId).collect(Collectors.toSet());
        parentIds = (ids.size() == 0) ?
                parentIds : parentIds.stream().filter(parentId -> !ids.contains(parentId)).collect(Collectors.toList());
        
        if (parentIds.size() != 0) {
            linkMapper.insertParents(childId, parentIds);
        }
    }

    // ---- Remove ---- //
    public void removeChildren(List<Long> parentIds, List<Long> childIds) {
        for (Long parentId : parentIds) {
            removeChildren(parentId, childIds);
        }
    }
    public void removeChildren(Long parentId, List<Long> childIds) {
        checkListPermission(parentId, true);
        linkMapper.deleteChildren(parentId, childIds);
        clean();
    }
    public void removeAllChildren(Long parentId) {
        checkListPermission(parentId, true);
        linkMapper.deleteAllChildren(parentId);
        clean();
    }
    public void removeParents(List<Long> childIds, List<Long> parentIds) {
        for (Long childId : childIds) {
            removeParents(childId, parentIds);
        }
    }
    public void removeParents(Long childId, List<Long> parentIds) {
        checkNodePermission(childId, true);
        linkMapper.deleteParents(permissionChecker.getUserId(), childId, parentIds);
        clean();
    }
    public void removeAllParents(Long childId) {
        checkNodePermission(childId, true);
        linkMapper.deleteAllParent(permissionChecker.getUserId(), childId);
        clean();
    }

    // ---- Set ---- //
    public void setChildren(Long parentId, List<Long> childIds) {
        checkListPermission(parentId, true);
        linkMapper.deleteAllChildren(parentId);
        addChildren(parentId, childIds);
        clean();
    }
    public void setParents(Long childId, List<Long> parentIds) {
        checkNodePermission(childId, true);
        linkMapper.deleteAllParent(permissionChecker.getUserId(), childId);
        addChildren(parentIds, Collections.singletonList(childId));
        clean();
    }

    // ---- Get ---- //
    public List<com.my.list.entity.Node> getChildren(Long parentId) {
        checkListPermission(parentId, false);
        return linkMapper.selectAllChildren(parentId)
            .stream().map(NodeImpl::new).collect(Collectors.toList());
    }
    public List<com.my.list.entity.Node> getParents(Long childId) {
        checkNodePermission(childId, false);
        return linkMapper.selectAllParent(permissionChecker.getUserId(), childId)
            .stream().map(NodeImpl::new).collect(Collectors.toList());
    }

    // ---- Check ---- //
    private MainData checkListPermission(Long listId, boolean write) {
        MainData listMainData = mainDataMapper.select(listId);
        if (listMainData == null) throw new DataException("No such list with listId=" + listId);
        permissionChecker.check(listMainData, write);
        return listMainData;
    }
    private MainData checkNodePermission(Long nodeId, boolean write) {
        MainData listMainData = mainDataMapper.select(nodeId);
        if (listMainData == null) throw new DataException("No such node with nodeId=" + nodeId);
        permissionChecker.check(listMainData, write);
        return listMainData;
    }
    
    // ---- Clean ---- //
    private void clean() {
        List<Long> ids = mainDataMapper.selectAllHangingIds();
        if (ids.size() > 0) {
            mainDataMapper.deleteAll(ids);
        }
    }

    // ---- Factory ---- //
    @Service
    public static class LinkServiceFactory {

        private final TypeManager typeManager;
        private final MainDataMapper mainDataMapper;
        private final LinkMapper linkMapper;

        public LinkServiceFactory(TypeManager typeManager, MainDataMapper mainDataMapper, LinkMapper linkMapper) {
            this.typeManager = typeManager;
            this.mainDataMapper = mainDataMapper;
            this.linkMapper = linkMapper;
        }

        public LinkService create(PermissionChecker permissionChecker) {
            return new LinkService(permissionChecker, typeManager, mainDataMapper, linkMapper);
        }
    }
}
