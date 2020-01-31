package com.my.list.service;

import com.my.list.domain.User;
import com.my.list.service.data.ListService;
import com.my.list.service.data.NodeService;
import com.my.list.service.data.PartService;
import org.springframework.stereotype.Service;

public class UserContext {

    public final User user;
    public final NodeService nodeService;
    public final ListService listService;
    public final PartService partService;

    public UserContext(User user, NodeService nodeService, ListService listService, PartService partService) {
        this.user = user;
        this.nodeService = nodeService;
        this.listService = listService;
        this.partService = partService;
    }

    @Service
    public static class UserContextFactory {

        private final PartService.PartServiceFactory partServiceFactory;
        private final NodeService.NodeServiceFactory nodeServiceFactory;
        private final ListService.ListServiceFactory listServiceFactory;

        public UserContextFactory(PartService.PartServiceFactory partServiceFactory, NodeService.NodeServiceFactory nodeServiceFactory, ListService.ListServiceFactory listServiceFactory) {
            this.partServiceFactory = partServiceFactory;
            this.nodeServiceFactory = nodeServiceFactory;
            this.listServiceFactory = listServiceFactory;
        }


        public UserContext create(User user) {
            PermissionChecker permissionChecker = new PermissionChecker(user);
            PartService partService = partServiceFactory.create(permissionChecker);
            NodeService nodeService = nodeServiceFactory.create(permissionChecker, partService);
            ListService listService = listServiceFactory.create(permissionChecker);
            return new UserContext(user, nodeService, listService, partService);
        }
        
    }
}
