package com.my.list.service;

import com.my.list.domain.User;
import com.my.list.dto.TypeConfig;
import org.springframework.stereotype.Service;

public class UserContext {

    public final User user;
    public final NodeService nodeService;

    public UserContext(User user, NodeService nodeService) {
        this.user = user;
        this.nodeService = nodeService;
    }

    @Service
    public static class UserContextFactory {

        private final TypeConfig typeConfig;
        private final MainDataService mainDataService;
        private final ExtraDataService extraDataService;
        private final ListDataService listDataService;

        public UserContextFactory(TypeConfig typeConfig, MainDataService mainDataService, ExtraDataService extraDataService, ListDataService listDataService) {
            this.typeConfig = typeConfig;
            this.mainDataService = mainDataService;
            this.extraDataService = extraDataService;
            this.listDataService = listDataService;
        }

        public UserContext create(User user) {
            NodeService nodeService = new NodeService(user, typeConfig, mainDataService, extraDataService, listDataService);
            return new UserContext(user, nodeService);
        }
        
    }
}
