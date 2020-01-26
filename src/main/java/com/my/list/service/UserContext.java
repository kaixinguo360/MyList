package com.my.list.service;

import com.my.list.domain.User;
import com.my.list.service.node.ListService;
import com.my.list.service.node.NodeService;
import com.my.list.service.search.SearchService;
import org.springframework.stereotype.Service;

public class UserContext {

    public final User user;
    public final NodeService nodeService;
    public final SearchService searchService;

    public UserContext(User user, NodeService nodeService, SearchService searchService) {
        this.user = user;
        this.nodeService = nodeService;
        this.searchService = searchService;
    }

    @Service
    public static class UserContextFactory {

        private final ListService.ListServiceFactory listServiceFactory;
        private final NodeService.NodeServiceFactory nodeServiceFactory;
        private final SearchService.SearchServiceFactory searchServiceFactory;

        public UserContextFactory(ListService.ListServiceFactory listServiceFactory, NodeService.NodeServiceFactory nodeServiceFactory, SearchService.SearchServiceFactory searchServiceFactory) {
            this.listServiceFactory = listServiceFactory;
            this.nodeServiceFactory = nodeServiceFactory;
            this.searchServiceFactory = searchServiceFactory;
        }


        public UserContext create(User user) {
            PermissionChecker permissionChecker = new PermissionChecker(user);
            ListService listService = listServiceFactory.create(permissionChecker);
            NodeService nodeService = nodeServiceFactory.create(permissionChecker, listService);
            SearchService searchService = searchServiceFactory.create(permissionChecker);
            return new UserContext(user, nodeService, searchService);
        }
        
    }
}
