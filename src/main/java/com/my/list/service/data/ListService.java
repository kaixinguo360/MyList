package com.my.list.service.data;

import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.service.PermissionChecker;
import com.my.list.service.filter.Filter;
import com.my.list.service.filter.FilterMapper;
import com.my.list.service.filter.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public class ListService {

    private final PermissionChecker permissionChecker;
    private final FilterMapper filterMapper;

    public ListService(PermissionChecker permissionChecker, FilterMapper filterMapper) {
        this.permissionChecker = permissionChecker;
        this.filterMapper = filterMapper;
    }
    
    public List<Node> getAll(Filter filter) {
        return filterMapper.getAll(permissionChecker.getUserId(), filter)
            .stream().map(NodeDTO::new).collect(Collectors.toList());
    }
    public List<Node> getAllByType(Filter filter, String type) {
        filter.addCondition("node_type", "=", "'" + type + "'");
        return getAll(filter);
    }
    public List<Node> getAllByListId(Filter filter, Long listId) {
        filter.addAndTag(new Tag(listId));
        return getAll(filter);
    }

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
