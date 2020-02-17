package com.my.list.service.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.service.PermissionChecker;
import com.my.list.service.filter.Filter;
import com.my.list.service.filter.FilterMapper;
import com.my.list.service.filter.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ListService {

    private final PermissionChecker permissionChecker;
    private final FilterMapper filterMapper;

    public ListService(PermissionChecker permissionChecker, FilterMapper filterMapper) {
        this.permissionChecker = permissionChecker;
        this.filterMapper = filterMapper;
    }

    public List<Node> getAllByType(Filter filter, String type) {
        filter.addCondition("node_type", "=", "'" + type + "'");
        return getAll(filter);
    }
    public List<Node> getAllByListId(Filter filter, Long listId) {
        filter.addAndTag(new Tag(listId));
        return getAll(filter);
    }
    public List<Node> getAll(Filter filter) {
        if (filter.getCascade() == null || !filter.getCascade())
            return getAllNoCascade(filter);
        else
            return getAllCascade(filter);
    }
    private List<Node> getAllNoCascade(Filter filter) {
        return filterMapper
            .getAll(permissionChecker.getUserId(), filter)
            .stream()
            .map(NodeDTO::new)
            .collect(Collectors.toList());
    }
    private List<Node> getAllCascade(Filter filter1) {
        Filter filter = new Filter();
        filter.setNotTags(filter1.getNotTags());
        filter.setAndTags(filter1.getAndTags());
        filter.setOrTags(filter1.getOrTags());
        
        List<Node> results = filterMapper
            .getAll(permissionChecker.getUserId(), filter)
            .stream()
            .map(NodeDTO::new)
            .collect(Collectors.toList());

        // else:
        Map<Long, Node> newNodes, allNodes = results.stream().collect(Collectors.toMap(
            node -> node.getMainData().getId(),
            node -> node
        ));
        Set<Tag> newTags = allNodes.values().stream()
            .filter(node -> node.getMainData().getCollection())
            .map(node -> new Tag(node.getMainData().getId()))
            .collect(Collectors.toSet());
        filter.setOrTags(null);
        filter.setAndTags(null);

        int count = 0;
        while (newTags.size() > 0) {
            count++;
            filter.setOrTags(newTags);
            List<com.my.list.domain.Node> tmp = filterMapper
                .getAll(permissionChecker.getUserId(), filter);
            newNodes = tmp
                .stream()
                .filter(node -> !allNodes.containsKey(node.getId()))
                .collect(Collectors.toMap(com.my.list.domain.Node::getId, NodeDTO::new));
            newTags = newNodes.values().stream()
                .filter(node -> node.getMainData().getCollection())
                .map(node -> new Tag(node.getMainData().getId()))
                .collect(Collectors.toSet());
            allNodes.putAll(newNodes);
        }
        List<Node> list = allNodes.values().stream().filter(filter1::filter).collect(Collectors.toList());
        
        try {
            System.out.println("============ Cascade Query ============");
            System.out.println(objectMapper.writeValueAsString(filter1));
            System.out.println("cycle: " + count + ", redundancy: " + (allNodes.size() - list.size()));
        } catch (JsonProcessingException ignored) {}
        
        return list;
    }
    ObjectMapper objectMapper = new ObjectMapper();

    // ---- Factory ---- //
    @Service
    public static class ListServiceFactory {

        private final FilterMapper searchService;

        public ListServiceFactory(FilterMapper searchService) {
            this.searchService = searchService;
        }

        public ListService create(PermissionChecker permissionChecker) {
            return new ListService(permissionChecker, searchService);
        }
    }

}
