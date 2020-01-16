package com.my.list.service.search;

import com.my.list.domain.*;
import com.my.list.dto.ListItem;
import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.service.UserContext;
import com.my.list.service.UserService;
import com.my.list.service.node.NodeService;
import com.my.list.type.image.Image;
import com.my.list.type.music.Music;
import com.my.list.type.text.Text;
import com.my.list.type.video.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class SearchServiceTest {

    @Autowired private ProcedureMapper procedureMapper;
    @Autowired private NodeMapper nodeMapper;
    @Autowired private UserService userService;

    private String token;

    private static Node textNode;
    private static Node imageNode;
    private static Node musicNode;
    private static Node videoNode;

    @BeforeEach
    void beforeAll() {
        User user = new User();
        Text text = new Text();
        Image image = new Image();
        Music music = new Music();
        Video video = new Video();
        
        user.setName("TestUser");
        user.setPass("1234567");
        user.setEmail("test@example.com");
        user.setStatus("activated");

        text.setContent("Test Content");

        image.setUrl("http://exmaple/image.png");
        image.setDescription("Test Image Description");

        music.setUrl("http://exmaple/music.mp3");
        music.setFormat("mp3");

        video.setUrl("http://exmaple/video.avi");
        video.setFormat("avi");

        // clean_all & add_user
        procedureMapper.clean_all();
        procedureMapper.add_user(user);
        
        // login
        token = userService.generateToken(user.getName(), user.getPass());
        
        // add_nodes
        NodeService nodeService = userService.getUserContext(token).nodeService;
        
        textNode = newNode(Text.TYPE_NAME, "Text Node", text);
        imageNode = newNode(Image.TYPE_NAME, "Image Node", image);
        musicNode = newNode(Music.TYPE_NAME, "Music Node", music);
        videoNode = newNode(Video.TYPE_NAME, "Video Node", video);
        textNode.getMainData().setLinkDelete(true);
        imageNode.getMainData().setLinkDelete(true);
        videoNode.getMainData().setHide(true);
        nodeService.add(textNode);
        nodeService.add(imageNode);
        nodeService.add(videoNode);
        
        Node listNode = newNode("list", "List Node", null);
        List<ListItem> list = new ArrayList<>();
        list.add(new ListItem(textNode, ListItem.ItemStatus.EXIST));
        list.add(new ListItem(imageNode, ListItem.ItemStatus.EXIST));
        list.add(new ListItem(musicNode, ListItem.ItemStatus.NEW));
        listNode.setExtraList(list);
        nodeService.add(listNode);
    }
    
    @Test
    void searchTest() {
        UserContext userContext = userService.getUserContext(token);
        NodeService nodeService = userContext.nodeService;
        SearchService searchService = userContext.searchService;

        Query query = new Query();
        query.addCondition("node_title", "like", "'%Node%'");
        query.addCondition("node_hide", "=", false);
        query.addSort("node_ctime", Sort.Direction.DESC);
        query.addSort("node_mtime", Sort.Direction.DESC);
        query.addSort("id", Sort.Direction.DESC);
        List<Node> nodes = searchService.simpleSearch(query);
        showNodes("Result", nodes);
    }

    private Node newNode(String type, String title, ExtraData extraData) {
        Node node = new NodeDTO();
        MainData mainData = node.getMainData();
        mainData.setType(type);
        mainData.setTitle(title);
        if (extraData != null) node.setExtraData(extraData);
        return node;
    }
    private void showNodes(String msg, List<Node> nodes) {
        System.out.println("==== " + msg + " [" + nodes.size() + "] ====");
        AtomicInteger i = new AtomicInteger();
        nodes.forEach(node -> System.out.println("[" + i.getAndIncrement() + "] " + node.getMainData()));
    }
    
}
