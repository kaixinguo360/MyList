package com.my.list.service;

import com.my.list.domain.NodeMapper;
import com.my.list.dto.Node;
import com.my.list.type.ExtraData;
import com.my.list.type.ExtraMapper;

import java.util.HashMap;
import java.util.Map;

public class ExtraNodeService extends SingleNodeService {
    
    public final Config config;
    
    public ExtraNodeService(Config config, NodeMapper nodeMapper) {
        super(nodeMapper);
        this.config = config;
    }

    @Override
    public void addNode(Node node) {
        Handler handler = config.getHandler(node.getType());
        super.addNode(node);
        if (handler.hasExtraData()) {
            ExtraData extraData = ExtraData.parse(handler.extraDataClass, node.getExtraData());
            extraData.setParentId(node.getId());
            handler.extraMapper.insert(extraData);
            node.setExtraData(extraData.toMap());
        }
    }

    @Override
    public Node getNode(Long id) {
        Node node = super.getNode(id);
        if (node == null) return null;
        Handler handler = config.getHandler(node.getType());
        if (handler.hasExtraData()) {
            ExtraData extraData = handler.extraMapper.selectByNodeId(node.getId());
            if (extraData == null) throw new DataException("Can't find extra data for node with id=" + id);
            node.setExtraData(extraData.toMap());
        }
        return node;
    }

    @Override
    public void updateNode(Node node) {
        Handler handler = config.getHandler(node.getType());
        if (handler.hasExtraData()) {
            ExtraData extraData = ExtraData.parse(handler.extraDataClass, node.getExtraData());
            if (extraData.getExtraId() == null) throw new DataException("Id of extra data is null");
            if (handler.extraMapper.selectByPrimaryKey(extraData.getExtraId()) == null) throw new DataException("Can't find extra data with id=" + extraData.getExtraId());
            super.updateNode(node);
            handler.extraMapper.updateByPrimaryKey(extraData);
        } else {
            super.updateNode(node);
        }
    }

    public static class Handler {
        
        public final String typeName;
        public final Class<? extends ExtraData> extraDataClass;
        public final ExtraMapper extraMapper;

        public Handler(String typeName) {
            this(typeName, null, null);
        }
        public Handler(String typeName, Class<? extends ExtraData> extraDataClass, ExtraMapper extraMapper) {
            this.typeName = typeName;
            this.extraDataClass = extraDataClass;
            this.extraMapper = extraMapper;
        }
        
        private boolean hasExtraData() {
            return extraDataClass != null;
        }
    }
    
    public static class Config {
        
        private Map<String, Handler> handlers = new HashMap<>();

        public void addHandler(Handler handler) {
            handlers.put(handler.typeName, handler);
        }

        private Handler getHandler(String typeName) {
            Handler handler = handlers.get(typeName);
            if (handler == null) throw new DataException("No such handler, typeName=" + typeName);
            return handler;
        }
    }
}
