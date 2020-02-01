package com.my.list.service.data;

import com.my.list.domain.Node;
import com.my.list.domain.NodeMapper;
import com.my.list.domain.PartMapper;
import com.my.list.dto.NodeDTO;
import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import com.my.list.exception.DataException;
import com.my.list.service.PermissionChecker;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PartService {

    private final PermissionChecker permissionChecker;

    private final TypeConfig typeConfig;
    private final NodeMapper nodeMapper;
    private final PartMapper partMapper;

    PartService(PermissionChecker permissionChecker, TypeConfig typeConfig, NodeMapper nodeMapper, PartMapper partMapper) {
        this.permissionChecker = permissionChecker;
        this.typeConfig = typeConfig;
        this.nodeMapper = nodeMapper;
        this.partMapper = partMapper;
    }

    public void addChildren(List<Long> parentId, List<Long> childIds) {
        for (Long listId : parentId) {
            addChildren(listId, childIds);
        }
    }
    public void addChildren(Long parentId, List<Long> childIds) {
        Node listNode = checkListPermission(parentId, true);
        Type type = typeConfig.getType(listNode);
        
        if (type.isExtraListUnique()) {
            Set<Long> ids = partMapper.selectAllChildren(parentId)
                .stream().map(Node::getId).collect(Collectors.toSet());
            childIds = (ids.size() == 0) ?
                childIds : childIds.stream().filter(partId -> !ids.contains(partId)).collect(Collectors.toList());
        }
        if (childIds.size() != 0) {
            partMapper.insertChildren(parentId, childIds);
        }
    }

    public void removeChildren(List<Long> parentId, List<Long> childIds) {
        for (Long listId : parentId) {
            removeChildren(listId, childIds);
        }
    }
    public void removeChildren(Long parentId, List<Long> childIds) {
        checkListPermission(parentId, true);
        partMapper.deleteChildren(parentId, childIds);
        partMapper.clean();
    }
    public void removeAllChildren(Long parentId) {
        checkListPermission(parentId, true);
        partMapper.deleteAllChildren(parentId);
        partMapper.clean();
    }

    public void setChildren(Long parentId, List<Long> childIds) {
        checkListPermission(parentId, true);
        partMapper.deleteAllChildren(parentId);
        addChildren(parentId, childIds);
        partMapper.clean();
    }
    public void setParents(Long childId, List<Long> parentIds) {
        checkNodePermission(childId, true);
        partMapper.deleteAllParent(permissionChecker.getUserId(), childId);
        addChildren(parentIds, Collections.singletonList(childId));
        partMapper.clean();
    }

    public List<com.my.list.dto.Node> getChildren(Long parentId) {
        checkListPermission(parentId, false);
        return partMapper.selectAllChildren(parentId)
            .stream().map(NodeDTO::new).collect(Collectors.toList());
    }
    public List<com.my.list.dto.Node> getParents(Long childId) {
        checkNodePermission(childId, false);
        return partMapper.selectAllParent(permissionChecker.getUserId(), childId)
            .stream().map(NodeDTO::new).collect(Collectors.toList());
    }
    
    private Node checkListPermission(Long listId, boolean write) {
        Node listNode = nodeMapper.selectByPrimaryKey(listId);
        if (listNode == null) throw new DataException("No such list with listId=" + listId);
        permissionChecker.check(listNode, write);
        return listNode;
    }
    private Node checkNodePermission(Long nodeId, boolean write) {
        Node listNode = nodeMapper.selectByPrimaryKey(nodeId);
        if (listNode == null) throw new DataException("No such node with nodeId=" + nodeId);
        permissionChecker.check(listNode, write);
        return listNode;
    }

    // ---- Factory ---- //
    @Service
    public static class PartServiceFactory {

        private final TypeConfig typeConfig;
        private final NodeMapper nodeMapper;
        private final PartMapper partMapper;

        public PartServiceFactory(TypeConfig typeConfig, NodeMapper nodeMapper, PartMapper partMapper) {
            this.typeConfig = typeConfig;
            this.nodeMapper = nodeMapper;
            this.partMapper = partMapper;
        }

        public PartService create(PermissionChecker permissionChecker) {
            return new PartService(permissionChecker, typeConfig, nodeMapper, partMapper);
        }
    }
}
