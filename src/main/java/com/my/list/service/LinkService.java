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
    public void addChildren(Long parentId, List<Long> childIds) {
        MainData parentMainData = checkNodePermission(parentId, true);
        TypeDefinition typeDefinition = typeManager.getType(parentMainData);
        if (childIds == null || childIds.size() == 0) {
            return;
        }

        if (typeDefinition.getExtraListUnique()) {
            Set<Long> ids = linkMapper.selectAllChildren(Collections.singletonList(parentId))
                .stream().map(MainData::getId).collect(Collectors.toSet());
            childIds = (ids.size() == 0)
                ? childIds
                : childIds.stream().filter(childId -> !ids.contains(childId)).collect(Collectors.toList());
        }
        if (childIds.size() != 0) {
            linkMapper.insertChildren(parentId, childIds);
        }
    }

    public void addParents(Long childId, List<Long> parentIds) {
        MainData inputChild = checkNodePermission(childId, true);
        if (parentIds == null || parentIds.size() == 0) {
            return;
        }

        List<MainData> inputParents = expandParents(parentIds, CascadeMode.NO_CASCADE, SoftMode.NO_SHADOW, null);
        List<Long> noCascadeParentIds = inputParents.stream().filter(n -> !typeManager.getType(n).getOtherDownCascade()).map(MainData::getId).collect(Collectors.toList());
        List<Long> cascadeParentIds = inputParents.stream().filter(n -> typeManager.getType(n).getOtherDownCascade()).map(MainData::getId).collect(Collectors.toList());

        // 不允许子节点级联更新的父结点
        if (noCascadeParentIds.size() != 0) {
            if (!typeManager.getType(inputChild).getOtherUpCascade()) {
                // 不允许父节点级联更新的子结点
                addParentsRaw(childId, noCascadeParentIds);
            } else {
                // 允许父节点级联更新的子结点
                List<Long> expandParentIds = expandParents(noCascadeParentIds, CascadeMode.ONLY_ALLOW_CASCADE, SoftMode.ALL_SHADOW, Collections.singletonList(childId))
                    .stream().map(MainData::getId).collect(Collectors.toList());

                addParentsRaw(childId, expandParentIds);
            }
        }

        // 允许子节点级联更新的父结点
        if (cascadeParentIds.size() != 0) {
            List<MainData> expandChilds = expandChildren(Collections.singletonList(childId), CascadeMode.ALL_CASCADE, SoftMode.CASCADE_SHADOW, cascadeParentIds);
            List<Long> noCascadeChildIds = expandChilds.stream().filter(n -> !typeManager.getType(n).getOtherUpCascade()).map(MainData::getId).collect(Collectors.toList());
            List<Long> cascadeChildIds = expandChilds.stream().filter(n -> typeManager.getType(n).getOtherUpCascade()).map(MainData::getId).collect(Collectors.toList());

            // 不允许父节点级联更新的子结点
            if (noCascadeChildIds.size() != 0) {
                for (Long childId1 : cascadeChildIds) {
                    addParentsRaw(childId1, cascadeParentIds);
                }
            }

            // 允许父节点级联更新的子结点
            if (cascadeChildIds.size() != 0) {
                List<Long> expandParentIds = expandParents(cascadeParentIds, CascadeMode.ONLY_ALLOW_CASCADE, SoftMode.ALL_SHADOW, cascadeChildIds)
                    .stream().map(MainData::getId).collect(Collectors.toList());

                for (Long childId1 : cascadeChildIds) {
                    addParentsRaw(childId1, expandParentIds);
                }
            }

        }
    }

    private void addParentsRaw(Long childId, List<Long> parentIds) {
        Set<Long> ids = linkMapper.selectAllParent(permissionChecker.getUserId(), Collections.singletonList(childId)).stream()
            .map(MainData::getId)
            .collect(Collectors.toSet());
        List<Long> ids1 = (ids.size() == 0)
            ? parentIds
            : parentIds.stream().filter(parentId -> !ids.contains(parentId)).collect(Collectors.toList());
        if (ids1.size() != 0) {
            linkMapper.insertParents(childId, ids1);
        }
    }

    // ---- Remove ---- //
    public void removeChildren(Long parentId, List<Long> childIds) {
        checkNodePermission(parentId, true);
        if (childIds == null || childIds.size() == 0) {
            return;
        }
        linkMapper.deleteChildren(parentId, childIds);
        clean();
    }

    public void removeAllChildren(Long parentId) {
        checkNodePermission(parentId, true);
        linkMapper.deleteAllChildren(parentId);
        clean();
    }

    public void removeParents(Long childId, List<Long> parentIds) {
        MainData inputChild = checkNodePermission(childId, true);
        if (parentIds == null || parentIds.size() == 0) {
            return;
        }

        List<MainData> inputParents = expandParents(parentIds, CascadeMode.NO_CASCADE, SoftMode.NO_SHADOW, null);
        List<Long> noCascadeParentIds = inputParents.stream().filter(n -> !typeManager.getType(n).getOtherDownCascade()).map(MainData::getId).collect(Collectors.toList());
        List<Long> cascadeParentIds = inputParents.stream().filter(n -> typeManager.getType(n).getOtherDownCascade()).map(MainData::getId).collect(Collectors.toList());

        // 不允许子节点级联更新的父结点
        if (noCascadeParentIds.size() != 0) {
            if (!typeManager.getType(inputChild).getOtherUpCascade()) {
                // 不允许父节点级联更新的子结点
                removeParentsRaw(childId, noCascadeParentIds);
            } else {
                // 允许父节点级联更新的子结点
                List<Long> expandParentIds = expandChildren(noCascadeParentIds, CascadeMode.ONLY_ALLOW_CASCADE, SoftMode.ALL_SHADOW, Collections.singletonList(childId))
                    .stream().map(MainData::getId).collect(Collectors.toList());

                removeParentsRaw(childId, expandParentIds);
            }
            linkMapper.deleteParents(permissionChecker.getUserId(), childId, noCascadeParentIds);
            clean();
        }

        // 允许子节点级联更新的父结点
        if (cascadeParentIds.size() != 0) {
            List<MainData> expandChilds = expandChildren(Collections.singletonList(childId), CascadeMode.ALL_CASCADE, SoftMode.CASCADE_SHADOW, cascadeParentIds);
            List<Long> noCascadeChildIds = expandChilds.stream().filter(n -> !typeManager.getType(n).getOtherUpCascade()).map(MainData::getId).collect(Collectors.toList());
            List<Long> cascadeChildIds = expandChilds.stream().filter(n -> typeManager.getType(n).getOtherUpCascade()).map(MainData::getId).collect(Collectors.toList());

            // 不允许父节点级联更新的子结点
            if (noCascadeChildIds.size() != 0) {
                for (Long childId1 : cascadeChildIds) {
                    removeParentsRaw(childId1, cascadeParentIds);
                }
            }

            // 允许父节点级联更新的子结点
            if (cascadeChildIds.size() != 0) {
                List<Long> expandParentIds = expandChildren(cascadeParentIds, CascadeMode.ONLY_ALLOW_CASCADE, SoftMode.ALL_SHADOW, cascadeChildIds)
                    .stream().map(MainData::getId).collect(Collectors.toList());

                if (expandParentIds.size() != 0) {
                    for (Long childId1 : cascadeChildIds) {
                        removeParentsRaw(childId1, expandParentIds);
                    }
                    clean();
                }
            }
        }
    }

    private void removeParentsRaw(Long childId, List<Long> parentIds) {
        linkMapper.deleteParents(permissionChecker.getUserId(), childId, parentIds);
    }

    public void removeAllParents(Long childId) {
        checkNodePermission(childId, true);
        linkMapper.deleteAllParent(permissionChecker.getUserId(), childId);
        clean();
    }

    // ---- Expand ---- //
    public List<MainData> expandChildren(List<Long> parentIds, CascadeMode cascadeMode, SoftMode softMode, List<Long> excludedIds) {
        if (parentIds == null || parentIds.size() == 0) {
            return new ArrayList<>();
        }

        List<MainData> allNodes = mainDataMapper.selectByIds(parentIds).stream()
            .filter(n -> permissionChecker.isAuthorized(n, true))
            .collect(Collectors.toList());

        if (cascadeMode != CascadeMode.NO_CASCADE) {

            List<Long> cascadeIds = allNodes.stream()
                .filter(n -> typeManager.getType(n).getSelfDownCascade())
                .map(MainData::getId)
                .collect(Collectors.toList());

            if (cascadeIds.size() != 0) {
                Set<Long> done = new HashSet<>();
                List<MainData> cascadeNodes = new ArrayList<>();

                while (cascadeIds.size() != 0) {
                    done.addAll(cascadeIds);
                    List<MainData> nodes = linkMapper.selectAllChildren(cascadeIds);
                    if (excludedIds != null) {
                        nodes = nodes.stream()
                            .filter(n -> !excludedIds.contains(n.getId()))
                            .collect(Collectors.toList());
                    }
                    switch (cascadeMode) {
                        case ALL_CASCADE:
                            cascadeNodes.addAll(nodes);
                            break;
                        case ONLY_ALLOW_CASCADE:
                            cascadeNodes.addAll(
                                nodes.stream()
                                    .filter(n -> typeManager.getType(n).getAllowCascade())
                                    .collect(Collectors.toList())
                            );
                            break;
                        case ONLY_NOT_ALLOW_CASCADE:
                            cascadeNodes.addAll(
                                nodes.stream()
                                    .filter(n -> !typeManager.getType(n).getAllowCascade())
                                    .collect(Collectors.toList())
                            );
                            break;
                    }
                    cascadeIds = nodes.stream()
                        .filter(n -> typeManager.getType(n).getSelfDownCascade())
                        .map(MainData::getId)
                        .filter(i -> !done.contains(i))
                        .collect(Collectors.toList());
                }

                if (softMode == SoftMode.CASCADE_SHADOW) {
                    cascadeNodes = cascadeNodes.stream()
                        .filter(i -> !typeManager.getType(i).getSoftCascade())
                        .collect(Collectors.toList());
                }

                allNodes.addAll(cascadeNodes);
            }
        }

        allNodes = allNodes.stream().distinct().collect(Collectors.toList());

        if (softMode == SoftMode.ALL_SHADOW) {
            allNodes = allNodes.stream()
                .filter(i -> !typeManager.getType(i).getSoftCascade())
                .collect(Collectors.toList());
        }

        if (excludedIds != null) {
            allNodes = allNodes.stream().filter(n -> !excludedIds.contains(n.getId())).collect(Collectors.toList());
        }

        return allNodes;
    }

    public List<MainData> expandParents(List<Long> childIds, CascadeMode cascadeMode, SoftMode softMode, List<Long> excludedIds) {
        if (childIds == null || childIds.size() == 0) {
            return new ArrayList<>();
        }

        List<MainData> allNodes = mainDataMapper.selectByIds(childIds).stream()
            .filter(n -> permissionChecker.isAuthorized(n, true))
            .collect(Collectors.toList());

        if (cascadeMode != CascadeMode.NO_CASCADE) {

            List<Long> cascadeIds = allNodes.stream()
                .filter(n -> typeManager.getType(n).getSelfUpCascade())
                .map(MainData::getId)
                .collect(Collectors.toList());

            if (cascadeIds.size() != 0) {
                Long userId = permissionChecker.getUserId();
                Set<Long> done = new HashSet<>();
                List<MainData> cascadeNodes = new ArrayList<>();

                while (cascadeIds.size() != 0) {
                    done.addAll(cascadeIds);
                    List<MainData> nodes = linkMapper.selectAllParent(userId, cascadeIds);
                    if (excludedIds != null) {
                        nodes = nodes.stream()
                            .filter(n -> !excludedIds.contains(n.getId()))
                            .collect(Collectors.toList());
                    }
                    switch (cascadeMode) {
                        case ALL_CASCADE:
                            cascadeNodes.addAll(nodes);
                            break;
                        case ONLY_ALLOW_CASCADE:
                            cascadeNodes.addAll(
                                nodes.stream()
                                    .filter(n -> typeManager.getType(n).getAllowCascade())
                                    .collect(Collectors.toList())
                            );
                            break;
                        case ONLY_NOT_ALLOW_CASCADE:
                            cascadeNodes.addAll(
                                nodes.stream()
                                    .filter(n -> !typeManager.getType(n).getAllowCascade())
                                    .collect(Collectors.toList())
                            );
                            break;
                    }
                    cascadeIds = nodes.stream()
                        .filter(n -> typeManager.getType(n).getSelfUpCascade())
                        .map(MainData::getId)
                        .filter(i -> !done.contains(i))
                        .collect(Collectors.toList());
                }

                if (softMode == SoftMode.CASCADE_SHADOW) {
                    cascadeNodes = cascadeNodes.stream()
                        .filter(i -> !typeManager.getType(i).getSoftCascade())
                        .collect(Collectors.toList());
                }

                allNodes.addAll(cascadeNodes);
            }
        }

        allNodes = allNodes.stream().distinct().collect(Collectors.toList());

        if (softMode == SoftMode.ALL_SHADOW) {
            allNodes = allNodes.stream()
                .filter(i -> !typeManager.getType(i).getSoftCascade())
                .collect(Collectors.toList());
        }

        if (excludedIds != null) {
            allNodes = allNodes.stream().filter(n -> !excludedIds.contains(n.getId())).collect(Collectors.toList());
        }

        return allNodes;
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

    public enum CascadeMode {
        NO_CASCADE,
        ALL_CASCADE,
        ONLY_ALLOW_CASCADE,
        ONLY_NOT_ALLOW_CASCADE,
    }

    public enum SoftMode {
        NO_SHADOW,
        ALL_SHADOW,
        CASCADE_SHADOW,
    }
}
