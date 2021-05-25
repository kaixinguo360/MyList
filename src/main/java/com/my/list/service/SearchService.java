package com.my.list.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.entity.MainData;
import com.my.list.entity.Node;
import com.my.list.entity.NodeImpl;
import com.my.list.entity.filter.Filter;
import com.my.list.entity.filter.Tag;
import com.my.list.mapper.SearchMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchService {

    private final PermissionChecker permissionChecker;
    private final SearchMapper searchMapper;

    public SearchService(PermissionChecker permissionChecker, SearchMapper searchMapper) {
        this.permissionChecker = permissionChecker;
        this.searchMapper = searchMapper;
    }

    public List<Node> getAllByType(Filter filter, String type) {
        filter.addCondition("content.node_type", "=", "'" + type + "'");
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
        return searchMapper
            .getAll(permissionChecker.getUserId(), filter)
            .stream()
            .map(NodeImpl::new)
            .collect(Collectors.toList());
    }
    private List<Node> getAllCascade(Filter filter1) {
        Filter filter = new Filter();
        filter.setNotTags(filter1.getNotTags());
        filter.setAndTags(filter1.getAndTags());
        filter.setOrTags(filter1.getOrTags());

        List<Node> results = searchMapper
            .getAll(permissionChecker.getUserId(), filter)
            .stream()
            .map(NodeImpl::new)
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
            List<MainData> tmp = searchMapper
                .getAll(permissionChecker.getUserId(), filter);
            newNodes = tmp
                .stream()
                .filter(node -> !allNodes.containsKey(node.getId()))
                .collect(Collectors.toMap(MainData::getId, NodeImpl::new));
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
    public static class SearchServiceFactory {

        private final SearchMapper searchService;

        public SearchServiceFactory(SearchMapper searchService) {
            this.searchService = searchService;
        }

        public SearchService create(PermissionChecker permissionChecker) {
            return new SearchService(permissionChecker, searchService);
        }
    }

}
