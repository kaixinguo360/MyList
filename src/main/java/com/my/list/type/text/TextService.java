package com.my.list.type.text;

import com.my.list.dto.Node;
import com.my.list.service.DataException;
import com.my.list.service.SingleNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TextService extends SingleNodeService {
    
    public static final String TYPE_NAME = "text";

    @Autowired private TextMapper textMapper;

    @Override
    public void addNode(Node node) {
        super.addNode(node);
        Text text = parse(node.getExtraValues());
        text.setNodeId(node.getId());
        textMapper.insert(text);
        node.setExtraValues(text.toMap());
    }

    @Override
    public Node getNode(Long id) {
        Node node = super.getNode(id);
        if (node == null) return null;
        if (!TYPE_NAME.equals(node.getType()))
            throw new DataException("Type mismatch, expected=" + TYPE_NAME + ", actual=" + node.getType());
        Text text = textMapper.selectByNodeId(node.getId());
        if (text == null) throw new DataException("Can't find extra data for node with id=" + id);
        node.setExtraValues(text.toMap());
        return node;
    }

    @Override
    public void updateNode(Node node) {
        Text text = parse(node.getExtraValues());
        if (text.getId() == null) throw new DataException("Id of extra data is null");
        if (textMapper.selectByPrimaryKey(text.getId()) == null) throw new DataException("Can't find extra data with id=" + text.getId());
        super.updateNode(node);
        textMapper.updateByPrimaryKey(text);
    }

    public static Text parse(Map<String, Object> map) {
        Text model = new Text();
        model.fromMap(map);
        return model;
    }
}
