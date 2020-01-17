package com.my.list.service.search;

import com.my.list.domain.ExtraData;
import com.my.list.domain.MainData;
import com.my.list.domain.ProcedureMapper;
import com.my.list.domain.User;
import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.service.UserContext;
import com.my.list.service.UserService;
import com.my.list.service.node.NodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class SearchServiceTest {

    @Autowired private ProcedureMapper procedureMapper;
    @Autowired private UserService userService;

    private String token1;
    private String token2;

    @BeforeEach
    void beforeAll() {
        User user1 = new User();
        User user2 = new User();

        user1.setName("TestUser");
        user1.setPass("1234567");
        user1.setEmail("test@example.com");
        user1.setStatus("activated");

        user2.setName("TestUser2");
        user2.setPass("1234567");
        user2.setEmail("test2@example.com");
        user2.setStatus("activated");

        // clean_all & add_user
        procedureMapper.clean_all();
        procedureMapper.add_user(user1);
        procedureMapper.add_user(user2);
        
        // login
        token1 = userService.generateToken(user1.getName(), user1.getPass());
        token2 = userService.generateToken(user2.getName(), user2.getPass());
        NodeService nodeService1 = userService.getUserContext(token1).nodeService;
        NodeService nodeService2 = userService.getUserContext(token2).nodeService;

        // create nodes
        List<Node> nodes1 = new ArrayList<>();
        for (int i=1; i<=5; i++) nodes1.add(newNode("Node1-" + i));
        
        List<Node> nodes2 = new ArrayList<>();
        for (int i=1; i<=5; i++) nodes2.add(newNode("Node2-" + i));

        nodes1.get(0).getMainData().setPermission("private");
        nodes1.get(1).getMainData().setPermission("protect");
        nodes1.get(2).getMainData().setPermission("public");
        nodes1.get(3).getMainData().setHide(true);
        nodes1.get(4).getMainData().setLike(true);

        nodes2.get(0).getMainData().setPermission("private");
        nodes2.get(1).getMainData().setPermission("protect");
        nodes2.get(2).getMainData().setPermission("public");
        nodes2.get(3).getMainData().setHide(true);
        nodes2.get(4).getMainData().setLike(true);

        // add nodes
        for (int i=1; i<=5; i++) nodeService1.add(nodes1.get(i-1));
        for (int i=1; i<=5; i++) nodeService2.add(nodes2.get(i-1));
    }

    @Test
    void conditionTest() {
        UserContext userContext = userService.getUserContext(token1);
        SearchService searchService = userContext.searchService;

        assertNodes("ALL", 10, new Query()
            .search(searchService));

        assertNodes("%Node1%", 5, new Query()
            .addCondition("node_title", "like", "'%Node1%'")
            .search(searchService));
        
        assertNodes("%Node% + hide=false", 8, new Query()
            .addCondition("node_title", "like", "'%Node%'")
            .setHide(false)
            .search(searchService));

        assertNodes("hide=true", 2, new Query()
            .setHide(true)
            .search(searchService));

        assertNodes("like=true", 2, new Query()
            .setLike(true)
            .search(searchService));
    }

    @Test
    void permissionTest() {
        UserContext userContext = userService.getUserContext(token1);
        SearchService searchService = userContext.searchService;

        // PRIVATE
        assertNodes("PRIVATE", 3, new Query()
            .setPermission(Permission.PRIVATE)
            .search(searchService));

        // PROTECT
        assertNodes("PROTECT", 1, new Query()
            .setPermission(Permission.PROTECT)
            .search(searchService));

        // PUBLIC
        assertNodes("PUBLIC", 1, new Query()
            .setPermission(Permission.PUBLIC)
            .search(searchService));

        // SHARED
        assertNodes("SHARED", 2, new Query()
            .setPermission(Permission.SHARED)
            .search(searchService));

        // SELF
        assertNodes("SELF", 5, new Query()
            .setPermission(Permission.SELF)
            .search(searchService));

        // OTHERS_PROTECT
        assertNodes("OTHERS_PROTECT", 1, new Query()
            .setPermission(Permission.OTHERS_PROTECT)
            .search(searchService));

        // OTHERS_PUBLIC
        assertNodes("OTHERS_PUBLIC", 1, new Query()
            .setPermission(Permission.OTHERS_PUBLIC)
            .search(searchService));

        // OTHERS_SHARED
        assertNodes("OTHERS_SHARED", 2, new Query()
            .setPermission(Permission.OTHERS_SHARED)
            .search(searchService));

        // EDITABLE
        assertNodes("EDITABLE", 6, new Query()
            .setPermission(Permission.EDITABLE)
            .search(searchService));

        // AVAILABLE
        assertNodes("AVAILABLE", 7, new Query()
            .setPermission(Permission.AVAILABLE)
            .search(searchService));
    }

    private Node newNode(String title) {
        return newNode("node", title, null);
    }
    private Node newNode(String type, String title, ExtraData extraData) {
        Node node = new NodeDTO();
        MainData mainData = node.getMainData();
        mainData.setType(type);
        mainData.setTitle(title);
        if (extraData != null) node.setExtraData(extraData);
        return node;
    }
    
    private void assertNodes(String message, int expectedSize, List<Node> nodes) {
        System.out.println("==== " + message + " [" + nodes.size() + "/" + expectedSize + "] ====");
        AtomicInteger i = new AtomicInteger();
        nodes.forEach(node -> System.out.println("[" + i.getAndIncrement() + "] "
            + node.getMainData().getUser() + " "
            + node.getMainData().getPermission() + " "
            + node.getMainData()));
        assertEquals(expectedSize, nodes.size());
    }
    
}
