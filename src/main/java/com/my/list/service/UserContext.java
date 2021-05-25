package com.my.list.service;

import com.my.list.entity.User;
import org.springframework.stereotype.Service;

public class UserContext {

    public final User user;
    public final NodeService nodeService;
    public final SearchService searchService;
    public final LinkService linkService;

    public UserContext(User user, NodeService nodeService, SearchService searchService, LinkService linkService) {
        this.user = user;
        this.nodeService = nodeService;
        this.searchService = searchService;
        this.linkService = linkService;
    }

    @Service
    public static class UserContextFactory {

        private final LinkService.LinkServiceFactory linkServiceFactory;
        private final NodeService.NodeServiceFactory nodeServiceFactory;
        private final SearchService.SearchServiceFactory searchServiceFactory;

        public UserContextFactory(LinkService.LinkServiceFactory linkServiceFactory, NodeService.NodeServiceFactory nodeServiceFactory, SearchService.SearchServiceFactory searchServiceFactory) {
            this.linkServiceFactory = linkServiceFactory;
            this.nodeServiceFactory = nodeServiceFactory;
            this.searchServiceFactory = searchServiceFactory;
        }


        public UserContext create(User user) {
            PermissionChecker permissionChecker = new PermissionChecker(user);
            LinkService linkService = linkServiceFactory.create(permissionChecker);
            NodeService nodeService = nodeServiceFactory.create(permissionChecker, linkService);
            SearchService searchService = searchServiceFactory.create(permissionChecker);
            return new UserContext(user, nodeService, searchService, linkService);
        }
        
    }
}
