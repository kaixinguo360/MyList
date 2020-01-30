package com.my.list.service.data;

import com.my.list.domain.Node;
import com.my.list.domain.NodeMapper;
import com.my.list.domain.Part;
import com.my.list.domain.PartMapper;
import com.my.list.dto.NodeDTO;
import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import com.my.list.exception.DataException;
import com.my.list.service.PermissionChecker;
import org.springframework.stereotype.Service;

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

    public void addParts(List<Long> listIds, List<Long> partIds) {
        for (Long listId : listIds) {
            addParts(listId, partIds);
        }
    }
    public void addParts(Long listId, List<Long> partIds) {
        Node listNode = checkPermission(listId, true);
        Type type = typeConfig.getType(listNode);
        
        if (type.isExtraListUnique()) {
            Set<Long> ids = partMapper.selectByListId(listId)
                .stream().map(Part::getContentId).collect(Collectors.toSet());
            partIds = (ids.size() == 0) ?
                partIds : partIds.stream().filter(partId -> !ids.contains(partId)).collect(Collectors.toList());
        }
        for (Long partId : partIds) {
            Part part = new Part();
            part.setParentId(listId);
            part.setContentId(partId);
            Integer count = partMapper.count(listId);
            part.setContentOrder((count == null) ? 0 : count);
            partMapper.insert(part);
        }
    }

    public void removeParts(List<Long> listIds, List<Long> partIds) {
        for (Long listId : listIds) {
            removeParts(listId, partIds);
        }
    }
    public void removeParts(Long listId, List<Long> partIds) {
        checkPermission(listId, true);
        partMapper.deleteByListIdAndPartIds(listId, partIds);
        nodeMapper.clean();
    }
    public void removeAllParts(Long listId) {
        checkPermission(listId, true);
        partMapper.deleteByListId(listId);
        nodeMapper.clean();
    }
    
    public void updateParts(Long listId, List<Long> partIds) {
        checkPermission(listId, true);
        partMapper.deleteByListId(listId);
        addParts(listId, partIds);
        nodeMapper.clean();
    }
    public List<com.my.list.dto.Node> getParts(Long listId) {
        checkPermission(listId, false);
        return nodeMapper.selectAllByListId(listId)
            .stream().map(NodeDTO::new).collect(Collectors.toList());
    }
    
    private Node checkPermission(Long listId, boolean write) {
        Node listNode = nodeMapper.selectByPrimaryKey(listId);
        if (listNode == null) throw new DataException("No such list with listId=" + listId);
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
