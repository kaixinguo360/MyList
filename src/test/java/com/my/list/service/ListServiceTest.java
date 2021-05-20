package com.my.list.service;

import com.my.list.TestUtil;
import com.my.list.domain.MainData;
import com.my.list.domain.User;
import com.my.list.dto.ListItem;
import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.service.data.ListService;
import com.my.list.service.data.NodeService;
import com.my.list.service.filter.Filter;
import com.my.list.service.filter.Permission;
import com.my.list.service.filter.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ListServiceTest {

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
        token1 = userService.generateToken(user1.getName(), user1.getPass());
        token2 = userService.generateToken(user2.getName(), user2.getPass());
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
        list1.add(new ListItem(nodes1.get(0), ListItem.ItemStatus.EXIST)); // self private
        list1.add(new ListItem(nodes1.get(1), ListItem.ItemStatus.EXIST)); // self protect
        list1.add(new ListItem(nodes1.get(2), ListItem.ItemStatus.EXIST)); // self public
        list1.add(new ListItem(nodes1.get(2), ListItem.ItemStatus.EXIST)); // -- (Duplicate data)
        list1.add(new ListItem(nodes2.get(1), ListItem.ItemStatus.EXIST)); // 2 protect
        list1.add(new ListItem(nodes2.get(2), ListItem.ItemStatus.EXIST)); // 2 public (tag3)
        list1.add(new ListItem(nodes2.get(2), ListItem.ItemStatus.EXIST)); // -- (Duplicate data)
        nodeService1.update(tagNode1, false); // (node1[3]) self private
        
        list2.add(new ListItem(nodes1.get(1), ListItem.ItemStatus.EXIST)); // self protect
        list2.add(new ListItem(nodes1.get(2), ListItem.ItemStatus.EXIST)); // self public
        list2.add(new ListItem(nodes1.get(3), ListItem.ItemStatus.EXIST)); // self private (tag1)
        list2.add(new ListItem(nodes1.get(3), ListItem.ItemStatus.EXIST)); // -- (Duplicate data)
        list2.add(new ListItem(nodes2.get(1), ListItem.ItemStatus.EXIST)); // 2 protect
        list2.add(new ListItem(nodes2.get(1), ListItem.ItemStatus.EXIST)); // -- (Duplicate data)
        nodeService1.update(tagNode2, false); // (node1[4]) self private
        
        list3.add(new ListItem(nodes1.get(1), ListItem.ItemStatus.EXIST)); // self protect
        list3.add(new ListItem(nodes1.get(2), ListItem.ItemStatus.EXIST)); // self public
        list3.add(new ListItem(nodes1.get(2), ListItem.ItemStatus.EXIST)); // -- (Duplicate data)
        list3.add(new ListItem(nodes2.get(0), ListItem.ItemStatus.EXIST)); // 2 private
        list3.add(new ListItem(nodes2.get(1), ListItem.ItemStatus.EXIST)); // 2 protect
        list3.add(new ListItem(nodes2.get(1), ListItem.ItemStatus.EXIST)); // -- (Duplicate data)
        nodeService2.update(tagNode3, false); // (node2[2]) 2 public
    }

    @Test
    void conditionTest() {
        UserContext userContext = userService.getUserContext(token1);
        ListService listService = userContext.listService;

        assertNodes("ALL", 5, new Filter()
            .getAll(listService));

        assertNodes("%Node1%", 5, new Filter()
            .addCondition("node_title", "like", "'%Node1%'")
            .getAll(listService));
        
        assertNodes("%Node%, hide=false", 4, new Filter()
            .addCondition("node_title", "like", "'%Node%'")
            .setHide(false)
            .getAll(listService));

        assertNodes("hide=true", 1, new Filter()
            .setHide(true)
            .getAll(listService));

        assertNodes("like=true", 1, new Filter()
            .setLike(true)
            .getAll(listService));
    }

    @Test
    void permissionTest() {
        UserContext userContext = userService.getUserContext(token1);
        ListService listService = userContext.listService;

        // PRIVATE
        assertNodes("PRIVATE", 3, new Filter()
            .setPermission(Permission.PRIVATE)
            .getAll(listService));

        // PROTECT
        assertNodes("PROTECT", 1, new Filter()
            .setPermission(Permission.PROTECT)
            .getAll(listService));

        // PUBLIC
        assertNodes("PUBLIC", 1, new Filter()
            .setPermission(Permission.PUBLIC)
            .getAll(listService));

        // SHARED
        assertNodes("SHARED", 2, new Filter()
            .setPermission(Permission.SHARED)
            .getAll(listService));

        // SELF
        assertNodes("SELF", 5, new Filter()
            .setPermission(Permission.SELF)
            .getAll(listService));

        // OTHERS_PROTECT
        assertNodes("OTHERS_PROTECT", 1, new Filter()
            .setPermission(Permission.OTHERS_PROTECT)
            .getAll(listService));

        // OTHERS_PUBLIC
        assertNodes("OTHERS_PUBLIC", 1, new Filter()
            .setPermission(Permission.OTHERS_PUBLIC)
            .getAll(listService));

        // OTHERS_SHARED
        assertNodes("OTHERS_SHARED", 2, new Filter()
            .setPermission(Permission.OTHERS_SHARED)
            .getAll(listService));

        // EDITABLE
        assertNodes("EDITABLE", 6, new Filter()
            .setPermission(Permission.EDITABLE)
            .getAll(listService));

        // AVAILABLE
        assertNodes("AVAILABLE", 7, new Filter()
            .setPermission(Permission.AVAILABLE)
            .getAll(listService));
    }
    
    @Test
    void tagTest() {
        UserContext userContext = userService.getUserContext(token1);
        ListService listService = userContext.listService;

        // or + title + strict
        assertNodes("orTag=$tag1, strict=true", 3, new Filter()
            .addOrTag(new Tag(tagNode1.getMainData().getTitle()))
            .getAll(listService));
        assertNodes("orTag=$tag2, strict=true", 3, new Filter()
            .addOrTag(new Tag(tagNode2.getMainData().getTitle()))
            .getAll(listService));
        assertNodes("orTag=$tag1+$tag2, strict=true", 4, new Filter()
            .addOrTag(new Tag(tagNode1.getMainData().getTitle()))
            .addOrTag(new Tag(tagNode2.getMainData().getTitle()))
            .getAll(listService));

        // or + id
        assertNodes("orTag=$tag1, use tag's id", 3, new Filter()
            .addOrTag(new Tag(tagNode1.getMainData().getId()))
            .getAll(listService));
        assertNodes("orTag=$tag2, use tag's id", 3, new Filter()
            .addOrTag(new Tag(tagNode2.getMainData().getId()))
            .getAll(listService));
        assertNodes("orTag=$tag1+$tag2, use tag's id", 4, new Filter()
            .addOrTag(new Tag(tagNode1.getMainData().getId()))
            .addOrTag(new Tag(tagNode2.getMainData().getId()))
            .getAll(listService));
        
        // or + title + not strict
        assertNodes("orTag=$tag1, strict=false", 3, new Filter()
            .addOrTag(new Tag(tagNode1.getMainData().getTitle(), false))
            .getAll(listService));
        assertNodes("orTag=$tag2, strict=false", 3, new Filter()
            .addOrTag(new Tag(tagNode2.getMainData().getTitle(), false))
            .getAll(listService));
        assertNodes("orTag=$tag1+$tag2, strict=false", 4, new Filter()
            .addOrTag(new Tag(tagNode1.getMainData().getTitle(), false))
            .addOrTag(new Tag(tagNode2.getMainData().getTitle(), false))
            .getAll(listService));
        assertNodes("orTag='Node', strict=false", 4, new Filter()
            .addOrTag(new Tag("Node", false))
            .getAll(listService));

        // not
        assertNodes("notTag=$tag1", 2, new Filter()
            .addNotTag(new Tag(tagNode1.getMainData().getId()))
            .getAll(listService));
        assertNodes("notTag='Node', strict=false", 1, new Filter()
            .addNotTag(new Tag("Node", false))
            .getAll(listService));
        
        // or + not
        assertNodes("or=tag1, not=tag2", 1, new Filter()
            .addOrTag(new Tag(tagNode1.getMainData().getId()))
            .addNotTag(new Tag(tagNode2.getMainData().getId()))
            .getAll(listService));

        // and
        assertNodes("andTag=tag1", 3, new Filter()
            .addAndTag(new Tag(tagNode1.getMainData().getId()))
            .getAll(listService));
        assertNodes("andTag=tag1+tag2", 2, new Filter()
            .addAndTag(new Tag(tagNode1.getMainData().getId()))
            .addAndTag(new Tag(tagNode2.getMainData().getId()))
            .getAll(listService));
        
        // others
        assertNodes("andTag=tag1, permission=AVAILABLE", 5, new Filter()
            .addAndTag(new Tag(tagNode1.getMainData().getId()))
            .setPermission(Permission.AVAILABLE)
            .getAll(listService));
        assertNodes("andTag=tag1+tag2, permission=AVAILABLE", 3, new Filter()
            .addAndTag(new Tag(tagNode1.getMainData().getId()))
            .addAndTag(new Tag(tagNode2.getMainData().getId()))
            .setPermission(Permission.AVAILABLE)
            .getAll(listService));
        assertNodes("orTag=tag1+tag2, permission=AVAILABLE", 6, new Filter()
            .addOrTag(new Tag(tagNode1.getMainData().getId()))
            .addOrTag(new Tag(tagNode2.getMainData().getId()))
            .setPermission(Permission.AVAILABLE)
            .getAll(listService));
        assertNodes("orTag=tag1, notTag=tag2, permission=AVAILABLE", 2, new Filter()
            .addOrTag(new Tag(tagNode1.getMainData().getId()))
            .addNotTag(new Tag(tagNode2.getMainData().getId()))
            .setPermission(Permission.AVAILABLE)
            .getAll(listService));
        assertNodes("orTag=tag3, permission=AVAILABLE", 3, new Filter()
            .addOrTag(new Tag(tagNode3.getMainData().getId()))
            .setPermission(Permission.AVAILABLE)
            .getAll(listService));
        assertNodes("orTag=tag3, permission=SELF", 2, new Filter()
            .addOrTag(new Tag(tagNode3.getMainData().getId()))
            .getAll(listService));
        assertNodes("orTag=tag3, permission=SELF", 0, new Filter()
            .addAndTag(new Tag(tagNode1.getMainData().getId()))
            .addOrTag(new Tag(tagNode2.getMainData().getId()))
            .addNotTag(new Tag(tagNode3.getMainData().getId()))
            .getAll(listService));
    }
    
    @Test
    public void otherTest() {
        UserContext userContext = userService.getUserContext(token1);
        ListService listService = userContext.listService;
        
        // getAllByType
        assertNodes("getAllByType='tag'", 2, listService.getAllByType(
            new Filter(), "tag"
        ));
        assertNodes("getAllByType='tag', andTag=$tagNode2", 1, listService.getAllByType(
            new Filter().addAndTag(new Tag(tagNode2.getMainData().getId())), "tag"
        ));
        assertNodes("getAllByType='tag', AVAILABLE", 3, listService.getAllByType(
            new Filter().setPermission(Permission.AVAILABLE), "tag"
        ));
        assertNodes("getAllByType='tag', andTag=$tagNode1", 0, listService.getAllByType(
            new Filter().addAndTag(new Tag(tagNode1.getMainData().getId())), "tag"
        ));
        assertNodes("getAllByType='tag', andTag=$tagNode1, AVAILABLE", 1, listService.getAllByType(
            new Filter().addAndTag(new Tag(tagNode1.getMainData().getId())).setPermission(Permission.AVAILABLE), "tag"
        ));
        assertNodes("getAllByType='tag', orTag=$tagNode1, AVAILABLE", 1, listService.getAllByType(
            new Filter().addOrTag(new Tag(tagNode1.getMainData().getId())).setPermission(Permission.AVAILABLE), "tag"
        ));

        // getAllByListId
        assertNodes("getAllByListId=$tagNode1", 3, listService.getAllByListId(
            new Filter(), tagNode1.getMainData().getId()
        ));
        assertNodes("getAllByListId=$tagNode2", 3, listService.getAllByListId(
            new Filter(), tagNode2.getMainData().getId()
        ));
        assertNodes("getAllByListId=$tagNode2, AVAILABLE", 4, listService.getAllByListId(
            new Filter().setPermission(Permission.AVAILABLE), tagNode2.getMainData().getId()
        ));
        assertNodes("getAllByListId=$tagNode3", 2, listService.getAllByListId(
            new Filter(), tagNode3.getMainData().getId()
        ));
        assertNodes("getAllByListId=$tagNode3, AVAILABLE", 3, listService.getAllByListId(
            new Filter().setPermission(Permission.AVAILABLE), tagNode3.getMainData().getId()
        ));
    }

    private Node newNode(String title) {
        Node node = new NodeDTO(com.my.list.domain.Node.defaultNode());
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
