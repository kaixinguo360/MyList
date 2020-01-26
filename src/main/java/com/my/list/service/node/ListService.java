package com.my.list.service.node;

import com.my.list.domain.Node;
import com.my.list.domain.NodeMapper;
import com.my.list.domain.Part;
import com.my.list.domain.PartMapper;
import com.my.list.dto.NodeDTO;
import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import com.my.list.service.DataException;
import com.my.list.service.PermissionChecker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListService {

    private final PermissionChecker permissionChecker;

    private final TypeConfig typeConfig;
    private final NodeMapper nodeMapper;
    private final PartMapper partMapper;

    ListService(PermissionChecker permissionChecker, TypeConfig typeConfig, NodeMapper nodeMapper, PartMapper partMapper) {
        this.permissionChecker = permissionChecker;
        this.typeConfig = typeConfig;
        this.nodeMapper = nodeMapper;
        this.partMapper = partMapper;
    }

    public void addPart(List<Long> listIds, List<Long> partIds) {
        for (Long listId : listIds) {
            addPart(listId, partIds);
        }
    }
    public void addPart(Long listId, List<Long> partIds) {
        Node listNode = checkPermission(listId);
        Type type = typeConfig.getType(listNode);
        
        if (type.isUniqueExtraList()) {
            Set<Long> ids = partMapper.selectByListId(listId)
                .stream().map(Part::getContentId).collect(Collectors.toSet());
            partIds = (ids.size() == 0) ?
                partIds : partIds.stream().filter(partId -> !ids.contains(partId)).collect(Collectors.toList());
        }
        for (Long partId : partIds) {
            insertPart(listId, partId);
        }
    }
    public void addPart(Long listId, Long partId) {
        Node listNode = checkPermission(listId);
        Type type = typeConfig.getType(listNode);
        
        if (type.isUniqueExtraList()) {
            Set<Long> ids = partMapper.selectByListId(listId)
                .stream().map(Part::getContentId).collect(Collectors.toSet());
            if (ids.size() != 0 && ids.contains(partId)) return;
        }
        insertPart(listId, partId);
    }
    public void insertPart(Long listId, Long partId) {
        Part part = new Part();
        part.setParentId(listId);
        part.setContentId(partId);
        Integer count = count(listId);
        part.setContentOrder((count == null) ? 0 : count);
        partMapper.insert(part);
    }
    
    public List<com.my.list.dto.Node> getList(Long listId) {
        checkPermission(listId);
        return nodeMapper.selectAllByListId(listId)
            .stream().map(NodeDTO::new).collect(Collectors.toList());
    }
    public void updateList(Long listId, List<Long> partIds) {
        checkPermission(listId);
        clean(listId);
        addPart(listId, partIds);
        nodeMapper.clean();
    }
    public void removeList(Long listId) {
        checkPermission(listId);
        clean(listId);
        nodeMapper.deleteByPrimaryKey(listId);
        nodeMapper.clean();
    }
    
    private void clean(Long listId) {
        partMapper.clean(listId);
    }
    private Integer count(Long listId) {
        return partMapper.count(listId);
    }
    private Node checkPermission(Long listId) {
        Node listNode = nodeMapper.selectByPrimaryKey(listId);
        if (listNode == null) throw new DataException("No such list with listId=" + listId);
        permissionChecker.check(listNode, false);
        return listNode;
    }

    // ---- Factory ---- //
    @Service
    public static class ListServiceFactory {

        private final TypeConfig typeConfig;
        private final NodeMapper nodeMapper;
        private final PartMapper partMapper;

        public ListServiceFactory(TypeConfig typeConfig, NodeMapper nodeMapper, PartMapper partMapper) {
            this.typeConfig = typeConfig;
            this.nodeMapper = nodeMapper;
            this.partMapper = partMapper;
        }

        public ListService create(PermissionChecker permissionChecker) {
            return new ListService(permissionChecker, typeConfig, nodeMapper, partMapper);
        }
    }
}
