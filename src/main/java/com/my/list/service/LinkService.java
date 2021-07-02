package com.my.list.service;

import com.my.list.entity.MainData;
import com.my.list.entity.NodeImpl;
import com.my.list.exception.DataException;
import com.my.list.mapper.LinkMapper;
import com.my.list.mapper.MainDataMapper;
import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import org.springframework.stereotype.Service;

import java.util.*;
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
        MainData parentMainData = checkNodePermission(parentId, true);
        TypeDefinition typeDefinition = typeManager.getType(parentMainData);
        
        if (typeDefinition.getExtraListUnique()) {
            Set<Long> ids = selectAllChildren(parentId)
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
        MainData childMainData = checkNodePermission(childId, true);
        TypeDefinition typeDefinition = typeManager.getType(childMainData);

        List<Long> childIds = Collections.singletonList(childId);
        if (typeDefinition.getCascade()) {
            List<MainData> childNodes = selectAllChildren(childIds, Mode.CASCADE, parentIds);
            childIds = childNodes.stream()
                .filter(n -> permissionChecker.isAuthorized(n, true))
                .map(MainData::getId)
                .collect(Collectors.toList());
            childIds.add(childId);
        }

        parentIds.addAll(
            selectAllParents(parentIds, Mode.ONLY_CASCADE, childIds)
                .stream().map(MainData::getId).collect(Collectors.toList())
        );

        List<Long> finalChildIds = childIds;
        parentIds = parentIds.stream()
            .filter(i -> !finalChildIds.contains(i))
            .collect(Collectors.toList());

        for (Long id : childIds) {
            Set<Long> ids = selectAllParents(id)
                .stream().map(MainData::getId).collect(Collectors.toSet());
            parentIds = (ids.size() == 0) ?
                parentIds : parentIds.stream().filter(parentId -> !ids.contains(parentId)).collect(Collectors.toList());

            if (parentIds.size() != 0) {
                linkMapper.insertParents(id, parentIds);
            }
        }
    }

    // ---- Remove ---- //
    public void removeChildren(List<Long> parentIds, List<Long> childIds) {
        for (Long parentId : parentIds) {
            removeChildren(parentId, childIds);
        }
    }
    public void removeChildren(Long parentId, List<Long> childIds) {
        checkNodePermission(parentId, true);
        linkMapper.deleteChildren(parentId, childIds);
        clean();
    }
    public void removeAllChildren(Long parentId) {
        checkNodePermission(parentId, true);
        linkMapper.deleteAllChildren(parentId);
        clean();
    }
    public void removeParents(List<Long> childIds, List<Long> parentIds) {
        for (Long childId : childIds) {
            removeParents(childId, parentIds);
        }
    }
    public void removeParents(Long childId, List<Long> parentIds) {
        MainData childMainData = checkNodePermission(childId, true);
        TypeDefinition typeDefinition = typeManager.getType(childMainData);

        List<Long> childIds = Collections.singletonList(childId);
        if (typeDefinition.getCascade()) {
            List<MainData> childNodes = selectAllChildren(childIds, Mode.CASCADE, parentIds);
            childIds = childNodes.stream()
                .filter(n -> permissionChecker.isAuthorized(n, true))
                .map(MainData::getId)
                .collect(Collectors.toList());
            childIds.add(childId);
        }

        parentIds.addAll(
            selectAllChildren(parentIds, Mode.ONLY_CASCADE, childIds)
                .stream().map(MainData::getId).collect(Collectors.toList())
        );

        for (Long id : childIds) {
            linkMapper.deleteParents(permissionChecker.getUserId(), id, parentIds);
        }

        clean();
    }
    public void removeAllParents(Long childId) {
        checkNodePermission(childId, true);
        linkMapper.deleteAllParent(permissionChecker.getUserId(), childId);
        clean();
    }

    // ---- Select ---- //
    public List<MainData> selectAllChildren(Long parentId) {
        return selectAllChildren(Collections.singletonList(parentId), Mode.NORMAL);
    }
    public List<MainData> selectAllChildren(List<Long> parentIds, Mode mode) {
        return selectAllChildren(parentIds, mode, null);
    }
    public List<MainData> selectAllChildren(List<Long> parentIds, Mode mode, List<Long> excludedIds) {
        if (mode == Mode.NORMAL) {
            return linkMapper.selectAllChildren(parentIds);
        } else {
            List<MainData> allNodes = new ArrayList<>();
            Set<Long> done = new HashSet<>();
            while (parentIds.size() != 0) {
                done.addAll(parentIds);
                List<MainData> nodes = linkMapper.selectAllChildren(parentIds);
                if (excludedIds != null) {
                    nodes = nodes.stream().filter(n -> !excludedIds.contains(n.getId())).collect(Collectors.toList());
                }
                if (mode == Mode.CASCADE) {
                    allNodes.addAll(nodes);
                } else {
                    allNodes.addAll(
                        nodes.stream()
                            .filter(n -> typeManager.getType(n).getCascade()).collect(Collectors.toList())
                    );
                }
                parentIds = nodes.stream()
                    .filter(n -> typeManager.getType(n).getCascade())
                    .map(MainData::getId)
                    .filter(i -> !done.contains(i))
                    .collect(Collectors.toList());
            }
            return allNodes;
        }
    }
    public List<MainData> selectAllParents(Long childId) {
        return selectAllChildren(Collections.singletonList(childId), Mode.NORMAL);
    }
    public List<MainData> selectAllParents(List<Long> childIds, Mode mode) {
        return selectAllParents(childIds, mode, null);
    }
    public List<MainData> selectAllParents(List<Long> childIds, Mode mode, List<Long> excludedIds) {
        if (mode == Mode.NORMAL) {
            return linkMapper.selectAllParent(permissionChecker.getUserId(), childIds);
        } else {
            Long userId = permissionChecker.getUserId();
            List<MainData> allNodes = new ArrayList<>();
            Set<Long> done = new HashSet<>();
            while (childIds.size() != 0) {
                done.addAll(childIds);
                List<MainData> nodes = linkMapper.selectAllParent(userId, childIds);
                if (excludedIds != null) {
                    nodes = nodes.stream().filter(n -> !excludedIds.contains(n.getId())).collect(Collectors.toList());
                }
                if (mode == Mode.CASCADE) {
                    allNodes.addAll(nodes);
                } else {
                    allNodes.addAll(
                        nodes.stream()
                            .filter(n -> typeManager.getType(n).getCascade()).collect(Collectors.toList())
                    );
                }
                childIds = nodes.stream()
                    .filter(n -> typeManager.getType(n).getCascade())
                    .map(MainData::getId)
                    .filter(i -> !done.contains(i))
                    .collect(Collectors.toList());
            }
            return allNodes;
        }
    }

    // ---- Set ---- //
    public void setChildren(Long parentId, List<Long> childIds) {
        checkNodePermission(parentId, true);
        linkMapper.deleteAllChildren(parentId);
        addChildren(parentId, childIds);
        clean();
    }
    public void setParents(Long childId, List<Long> parentIds) {
        checkNodePermission(childId, true);
        linkMapper.deleteAllParent(permissionChecker.getUserId(), childId);
        addParents(childId, parentIds);
        clean();
    }

    // ---- Get ---- //
    public List<com.my.list.entity.Node> getChildren(Long parentId) {
        checkNodePermission(parentId, false);
        return linkMapper.selectAllChildren(Collections.singletonList(parentId))
            .stream().map(NodeImpl::new).collect(Collectors.toList());
    }
    public List<com.my.list.entity.Node> getParents(Long childId) {
        checkNodePermission(childId, false);
        return linkMapper.selectAllParent(permissionChecker.getUserId(), Collections.singletonList(childId))
            .stream().map(NodeImpl::new).collect(Collectors.toList());
    }

    // ---- Check ---- //
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

    public enum Mode {
        NORMAL,
        CASCADE,
        ONLY_CASCADE,
    }
}
