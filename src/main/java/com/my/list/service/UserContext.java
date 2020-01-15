package com.my.list.service;

import com.my.list.domain.NodeMapper;
import com.my.list.domain.ProcedureMapper;
import com.my.list.domain.User;
import org.springframework.stereotype.Service;

public class UserContext {

    public final User user;
    public final SingleNodeService singleNodeService;
    public final ExtraNodeService extraNodeService;
    
    public UserContext(User user, SingleNodeService singleNodeService, ExtraNodeService extraNodeService) {
        this.user = user;
        this.singleNodeService = singleNodeService;
        this.extraNodeService = extraNodeService;
    }

    @Service
    public static class UserContextFactory {
    
        private final TypeConfig typeConfig;
        private final NodeMapper nodeMapper;
        private final ProcedureMapper procedureMapper;
        
        public UserContextFactory(TypeConfig typeConfig, NodeMapper nodeMapper, ProcedureMapper procedureMapper) {
            this.typeConfig = typeConfig;
            this.nodeMapper = nodeMapper;
            this.procedureMapper = procedureMapper;
        }
    
        public UserContext create(User user) {
            SingleNodeService singleNodeService = new SingleNodeService(user, nodeMapper);
            ExtraNodeService extraNodeService = new ExtraNodeService(user, typeConfig, singleNodeService, nodeMapper, procedureMapper);
            return new UserContext(user, singleNodeService, extraNodeService);
        }
        
    }
}
