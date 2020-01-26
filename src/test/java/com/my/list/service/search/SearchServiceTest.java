package com.my.list.service.search;

import com.my.list.TestUtil;
import com.my.list.domain.MainData;
import com.my.list.domain.User;
import com.my.list.dto.ListItem;
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

    @Autowired private TestUtil testUtil;
    @Autowired private UserService userService;

    private String token1;
    private String token2;

    private Node tagNode1;
    private Node tagNode2;
    private Node tagNode3;

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
        testUtil.clean_all();
        userService.add(user1);
        userService.add(user2);
        
        // login
        token1 = userService.generateToken(user1.getName(), user1.getPass(), false);
        token2 = userService.generateToken(user2.getName(), user2.getPass(), false);
        NodeService nodeService1 = userService.getUserContext(token1).nodeService;
        NodeService nodeService2 = userService.getUserContext(token2).nodeService;

        // create nodes
        List<Node> nodes1 = new ArrayList<>();
        List<Node> nodes2 = new ArrayList<>();
        for (int i=1; i<=5; i++) nodes1.add(newNode("Node1-" + i));
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

        // create tags
        tagNode1 = nodes1.get(3);
        tagNode1.getMainData().setType("tag");
        List<ListItem> list1 = new ArrayList<>();
        tagNode1.setExtraList(list1);
        
        tagNode2 = nodes1.get(4);
        tagNode2.getMainData().setType("tag");
        List<ListItem> list2 = new ArrayList<>();
        tagNode2.setExtraList(list2);
        
        tagNode3 = nodes2.get(2);
        tagNode3.getMainData().setType("tag");
        List<ListItem> list3 = new ArrayList<>();
        tagNode3.setExtraList(list3);

        // add nodes
        for (int i=1; i<=5; i++) nodeService1.add(nodes1.get(i-1));
        for (int i=1; i<=5; i++) nodeService2.add(nodes2.get(i-1));

        // add tags
        list1.add(new ListItem(nodes1.get(0), ListItem.ItemStatus.EXIST));
        list1.add(new ListItem(nodes1.get(1), ListItem.ItemStatus.EXIST));
        list1.add(new ListItem(nodes1.get(2), ListItem.ItemStatus.EXIST));
        list1.add(new ListItem(nodes1.get(2), ListItem.ItemStatus.EXIST)); // Duplicate data
        list1.add(new ListItem(nodes2.get(1), ListItem.ItemStatus.EXIST));
        list1.add(new ListItem(nodes2.get(2), ListItem.ItemStatus.EXIST));
        list1.add(new ListItem(nodes2.get(2), ListItem.ItemStatus.EXIST)); // Duplicate data
        nodeService1.update(tagNode1);
        
        list2.add(new ListItem(nodes1.get(1), ListItem.ItemStatus.EXIST));
        list2.add(new ListItem(nodes1.get(2), ListItem.ItemStatus.EXIST));
        list2.add(new ListItem(nodes1.get(3), ListItem.ItemStatus.EXIST));
        list2.add(new ListItem(nodes1.get(3), ListItem.ItemStatus.EXIST)); // Duplicate data
        list2.add(new ListItem(nodes2.get(1), ListItem.ItemStatus.EXIST));
        list2.add(new ListItem(nodes2.get(1), ListItem.ItemStatus.EXIST)); // Duplicate data
        nodeService1.update(tagNode2);
        
        list3.add(new ListItem(nodes1.get(1), ListItem.ItemStatus.EXIST));
        list3.add(new ListItem(nodes1.get(2), ListItem.ItemStatus.EXIST));
        list3.add(new ListItem(nodes1.get(2), ListItem.ItemStatus.EXIST)); // Duplicate data
        list3.add(new ListItem(nodes2.get(0), ListItem.ItemStatus.EXIST));
        list3.add(new ListItem(nodes2.get(1), ListItem.ItemStatus.EXIST));
        list3.add(new ListItem(nodes2.get(1), ListItem.ItemStatus.EXIST)); // Duplicate data
        nodeService2.update(tagNode3);
    }

    @Test
    void conditionTest() {
        UserContext userContext = userService.getUserContext(token1);
        SearchService searchService = userContext.searchService;

        assertNodes("ALL", 5, new Query()
            .search(searchService));

        assertNodes("%Node1%", 5, new Query()
            .addCondition("node_title", "like", "'%Node1%'")
            .search(searchService));
        
        assertNodes("%Node%, hide=false", 4, new Query()
            .addCondition("node_title", "like", "'%Node%'")
            .setHide(false)
            .search(searchService));

        assertNodes("hide=true", 1, new Query()
            .setHide(true)
            .search(searchService));

        assertNodes("like=true", 1, new Query()
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
    
    @Test
    void tagTest() {
        UserContext userContext = userService.getUserContext(token1);
        SearchService searchService = userContext.searchService;

        // or + title + strict
        assertNodes("orTag=$tag1, strict=true", 3, new Query()
            .addOrTag(new Tag(tagNode1.getMainData().getTitle()))
            .search(searchService));
        assertNodes("orTag=$tag2, strict=true", 3, new Query()
            .addOrTag(new Tag(tagNode2.getMainData().getTitle()))
            .search(searchService));
        assertNodes("orTag=$tag1+$tag2, strict=true", 4, new Query()
            .addOrTag(new Tag(tagNode1.getMainData().getTitle()))
            .addOrTag(new Tag(tagNode2.getMainData().getTitle()))
            .search(searchService));

        // or + id
        assertNodes("orTag=$tag1, use tag's id", 3, new Query()
            .addOrTag(new Tag(tagNode1.getMainData().getId()))
            .search(searchService));
        assertNodes("orTag=$tag2, use tag's id", 3, new Query()
            .addOrTag(new Tag(tagNode2.getMainData().getId()))
            .search(searchService));
        assertNodes("orTag=$tag1+$tag2, use tag's id", 4, new Query()
            .addOrTag(new Tag(tagNode1.getMainData().getId()))
            .addOrTag(new Tag(tagNode2.getMainData().getId()))
            .search(searchService));
        
        // or + title + not strict
        assertNodes("orTag=$tag1, strict=false", 3, new Query()
            .addOrTag(new Tag(tagNode1.getMainData().getTitle(), false))
            .search(searchService));
        assertNodes("orTag=$tag2, strict=false", 3, new Query()
            .addOrTag(new Tag(tagNode2.getMainData().getTitle(), false))
            .search(searchService));
        assertNodes("orTag=$tag1+$tag2, strict=false", 4, new Query()
            .addOrTag(new Tag(tagNode1.getMainData().getTitle(), false))
            .addOrTag(new Tag(tagNode2.getMainData().getTitle(), false))
            .search(searchService));
        assertNodes("orTag='Node', strict=false", 4, new Query()
            .addOrTag(new Tag("Node", false))
            .search(searchService));

        // not
        assertNodes("notTag=$tag1", 2, new Query()
            .addNotTag(new Tag(tagNode1.getMainData().getId()))
            .search(searchService));
        assertNodes("notTag='Node', strict=false", 1, new Query()
            .addNotTag(new Tag("Node", false))
            .search(searchService));
        
        // or + not
        assertNodes("or=tag1, not=tag2", 1, new Query()
            .addOrTag(new Tag(tagNode1.getMainData().getId()))
            .addNotTag(new Tag(tagNode2.getMainData().getId()))
            .search(searchService));

        // and
        assertNodes("andTag=tag1", 3, new Query()
            .addAndTag(new Tag(tagNode1.getMainData().getId()))
            .search(searchService));
        assertNodes("andTag=tag1+tag2", 2, new Query()
            .addAndTag(new Tag(tagNode1.getMainData().getId()))
            .addAndTag(new Tag(tagNode2.getMainData().getId()))
            .search(searchService));
        
        // others
        assertNodes("andTag=tag1, permission=AVAILABLE", 5, new Query()
            .addAndTag(new Tag(tagNode1.getMainData().getId()))
            .setPermission(Permission.AVAILABLE)
            .search(searchService));
        assertNodes("andTag=tag1+tag2, permission=AVAILABLE", 3, new Query()
            .addAndTag(new Tag(tagNode1.getMainData().getId()))
            .addAndTag(new Tag(tagNode2.getMainData().getId()))
            .setPermission(Permission.AVAILABLE)
            .search(searchService));
        assertNodes("orTag=tag1+tag2, permission=AVAILABLE", 6, new Query()
            .addOrTag(new Tag(tagNode1.getMainData().getId()))
            .addOrTag(new Tag(tagNode2.getMainData().getId()))
            .setPermission(Permission.AVAILABLE)
            .search(searchService));
        assertNodes("orTag=tag1, notTag=tag2, permission=AVAILABLE", 2, new Query()
            .addOrTag(new Tag(tagNode1.getMainData().getId()))
            .addNotTag(new Tag(tagNode2.getMainData().getId()))
            .setPermission(Permission.AVAILABLE)
            .search(searchService));
        assertNodes("orTag=tag3, permission=AVAILABLE", 3, new Query()
            .addOrTag(new Tag(tagNode3.getMainData().getId()))
            .setPermission(Permission.AVAILABLE)
            .search(searchService));
        assertNodes("orTag=tag3, permission=SELF", 2, new Query()
            .addOrTag(new Tag(tagNode3.getMainData().getId()))
            .search(searchService));
        assertNodes("orTag=tag3, permission=SELF", 0, new Query()
            .addAndTag(new Tag(tagNode1.getMainData().getId()))
            .addOrTag(new Tag(tagNode2.getMainData().getId()))
            .addNotTag(new Tag(tagNode3.getMainData().getId()))
            .search(searchService));
    }

    private Node newNode(String title) {
        Node node = new NodeDTO(com.my.list.domain.Node.Companion.defaultNode());
        MainData mainData = node.getMainData();
        mainData.setType("node");
        mainData.setTitle(title);
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
