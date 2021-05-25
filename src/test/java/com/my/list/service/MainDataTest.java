package com.my.list.service;

import com.my.list.TestUtil;
import com.my.list.entity.MainData;
import com.my.list.entity.Node;
import com.my.list.entity.NodeImpl;
import com.my.list.entity.User;
import com.my.list.mapper.MainDataMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MainDataTest {

    @Autowired private TestUtil testUtil;
    @Autowired private MainDataMapper mainDataMapper;
    @Autowired private UserService userService;

    private String token;

    @BeforeEach
    void beforeAll() {
        User user = new User();
        user.setName("TestUser");
        user.setPass("1234567");
        user.setEmail("test@example.com");
        user.setStatus("activated");
        
        // clean_all & add_user
        testUtil.clean_all();
        userService.add(user);
        
        // login
        token = userService.generateToken(user.getName(), user.getPass());
    }

    @Test
    void mainDataTest() {
        NodeService nodeService = userService.getUserContext(token).nodeService;
        
        // addNode
        Node node = newNode();
        MainData mainData = node.getMainData();
        nodeService.add(node);
        assertEquals(1, mainDataMapper.selectAll().size());

        // getNode
        Node node1 = nodeService.get(mainData.getId());
        assertEquals(mainData.getType(), node1.getMainData().getType());

        // updateNode
        mainData.setComment("This is comment.");
        nodeService.update(node, false);
        assertEquals(mainData.getComment(), nodeService.get(mainData.getId()).getMainData().getComment());

        // removeNode
        nodeService.remove(mainData.getId());
        assertEquals(0, mainDataMapper.selectAll().size());
    }

    private Node newNode() {
        Node node = new NodeImpl(MainData.defaultNode());
        MainData mainData = node.getMainData();
        mainData.setType("node");
        mainData.setTitle("Simple Node");
        return node;
    }
    
}
