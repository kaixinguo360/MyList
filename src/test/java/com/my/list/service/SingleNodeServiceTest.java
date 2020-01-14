package com.my.list.service;

import com.my.list.domain.Node;
import com.my.list.domain.NodeMapper;
import com.my.list.domain.ProcedureMapper;
import com.my.list.domain.User;
import com.my.list.dto.SingleNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class SingleNodeServiceTest {

    @Autowired private ProcedureMapper procedureMapper;
    @Autowired private NodeMapper nodeMapper;

    @Autowired private SingleNodeService singleNodeService;

    private static User user = new User();

    @BeforeEach
    void beforeAll() {
        user.setName("TestUser");
        user.setPass("1234567");
        user.setEmail("test@example.com");
        user.setStatus("activated");
        
        // clean_all & add_user
        procedureMapper.clean_all();
        procedureMapper.add_user(user);
    }

    @Test
    void singleNodeService() {
        // addNode
        SingleNode singleNode = newNode();
        singleNodeService.add(singleNode);
        assertEquals(1, nodeMapper.selectAll().size());

        // getNode
        SingleNode singleNode1 = singleNodeService.get(singleNode.getId());
        assertEquals(singleNode.getType(), singleNode1.getType());

        // updateNode
        singleNode.setComment("This is comment.");
        singleNodeService.update(singleNode);
        assertEquals(singleNode.getComment(), singleNodeService.get(singleNode.getId()).getComment());

        // removeNode
        singleNodeService.remove(singleNode.getId());
        assertEquals(0, nodeMapper.selectAll().size());
    }

    private SingleNode newNode() {
        SingleNode singleNode = new Node();
        singleNode.setUser(user.getId());
        singleNode.setType("node");
        singleNode.setTitle("Single Node");
        return singleNode;
    }
    
}
