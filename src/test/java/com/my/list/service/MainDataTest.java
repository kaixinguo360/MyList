package com.my.list.service;

import com.my.list.TestUtil;
import com.my.list.domain.MainData;
import com.my.list.domain.NodeMapper;
import com.my.list.domain.User;
import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.service.data.NodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MainDataTest {

    @Autowired private TestUtil testUtil;
    @Autowired private NodeMapper nodeMapper;
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
        assertEquals(1, nodeMapper.selectAll().size());

        // getNode
        Node node1 = nodeService.get(mainData.getId());
        assertEquals(mainData.getType(), node1.getMainData().getType());

        // updateNode
        mainData.setComment("This is comment.");
        nodeService.update(node, false);
        assertEquals(mainData.getComment(), nodeService.get(mainData.getId()).getMainData().getComment());

        // removeNode
        nodeService.remove(mainData.getId());
        assertEquals(0, nodeMapper.selectAll().size());
    }

    private Node newNode() {
        Node node = new NodeDTO(com.my.list.domain.Node.defaultNode());
        MainData mainData = node.getMainData();
        mainData.setType("node");
        mainData.setTitle("Simple Node");
        return node;
    }
    
}
