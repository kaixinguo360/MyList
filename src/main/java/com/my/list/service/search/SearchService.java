package com.my.list.service.search;

import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.service.PermissionChecker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public class SearchService {

    private final PermissionChecker permissionChecker;
    private final SearchMapper searchMapper;

    public SearchService(PermissionChecker permissionChecker, SearchMapper searchMapper) {
        this.permissionChecker = permissionChecker;
        this.searchMapper = searchMapper;
    }

    public List<Node> search(Query query) {
        return searchMapper.search(permissionChecker.getUserId(), query)
            .stream().map(NodeDTO::new).collect(Collectors.toList());
    }

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
