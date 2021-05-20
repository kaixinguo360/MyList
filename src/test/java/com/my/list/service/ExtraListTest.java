package com.my.list.service;

import com.my.list.TestUtil;
import com.my.list.domain.ExtraData;
import com.my.list.domain.MainData;
import com.my.list.domain.NodeMapper;
import com.my.list.domain.User;
import com.my.list.dto.ListItem;
import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.service.data.NodeService;
import com.my.list.type.image.Image;
import com.my.list.type.image.ImageConfig;
import com.my.list.type.music.Music;
import com.my.list.type.music.MusicConfig;
import com.my.list.type.text.Text;
import com.my.list.type.text.TextConfig;
import com.my.list.type.video.Video;
import com.my.list.type.video.VideoConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ExtraListTest {

    @Autowired private TestUtil testUtil;
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
        image.setGallery("Test Image Description");

        music.setUrl("http://exmaple/music.mp3");
        music.setFormat("mp3");

        video.setUrl("http://exmaple/video.avi");
        video.setFormat("avi");

        // clean_all & add_user
        testUtil.clean_all();
        userService.add(user);
        
        // login
        token = userService.generateToken(user.getName(), user.getPass());
        
        // add_nodes
        //
        // nodes(2): name       lcount  lstatus
        //    [ ]    listNode   0       alone
        //    [+]    textNode   0       attachment
        //    [+]    imageNode  0       attachment
        //    [ ]    musicNode  0       alone
        //    [ ]    videoNode  0       alone
        textNode = newNode(TextConfig.TYPE_NAME, "Text Node", text);
        imageNode = newNode(ImageConfig.TYPE_NAME, "Image Node", image);
        musicNode = newNode(MusicConfig.TYPE_NAME, "Music Node", music);
        videoNode = newNode(VideoConfig.TYPE_NAME, "Video Node", video);
        textNode.getMainData().setPart(true);
        imageNode.getMainData().setPart(true);
        NodeService nodeService = userService.getUserContext(token).nodeService;
        nodeService.add(textNode);
        nodeService.add(imageNode);
    }
    
    @Test
    void listDataTest() {
        NodeService nodeService = userService.getUserContext(token).nodeService;
        
        // add list
        //
        // list(4):
        //    [0]  EXIST textNode
        //    [1]  EXIST imageNode
        //    [2]  NEW   musicNode
        //
        // nodes(4): name       lcount  lstatus
        //    [+]    listNode   0       alone
        //    [=]    textNode   0 -> 1  attachment
        //    [=]    imageNode  0 -> 1  attachment
        //    [+]    musicNode  0 -> 1  alone
        //    [ ]    videoNode  0       alone
        Node listNode = newNode("list", "List Node", null);
        List<ListItem> list = new ArrayList<>();
        list.add(new ListItem(textNode, ListItem.ItemStatus.EXIST));
        list.add(new ListItem(imageNode, ListItem.ItemStatus.EXIST));
        list.add(new ListItem(musicNode, ListItem.ItemStatus.NEW));
        listNode.setExtraList(list);
        nodeService.add(listNode);
        
        // get list
        Node result = nodeService.get(listNode.getMainData().getId());
        list = result.getExtraList();
        showList("Get result", list);
        assertEquals(3, list.size());
        assertEquals(textNode.getMainData().getTitle(), list.get(0).node.getMainData().getTitle());
        assertEquals(imageNode.getMainData().getTitle(), list.get(1).node.getMainData().getTitle());
        assertEquals(musicNode.getMainData().getTitle(), list.get(2).node.getMainData().getTitle());
        assertEquals(4, nodeMapper.selectAll().size());

        // update list
        //
        // list(4):
        //    [0]  EXIST  imageNode
        //    [1]  EXIST  musicNode
        //    [2]  NEW    videoNode
        //    [3]  UPDATE imageNode
        //
        // nodes(4): name       lcount  lstatus
        //    [=]    listNode   0       alone
        //    [-]    textNode   1 -> 0  attachment
        //    [C]    imageNode  1 -> 2  attachment
        //    [=]    musicNode  1 -> 1  alone
        //    [+]    videoNode  0 -> 1  alone
        list.remove(0);
        list.add(new ListItem(videoNode, ListItem.ItemStatus.NEW));
        list.add(new ListItem(imageNode, ListItem.ItemStatus.EXIST));
        Node updatedImageNode = nodeService.get(list.get(3).node.getMainData().getId());
        updatedImageNode.getMainData().setTitle("Updated Image Node");
        list.set(3, new ListItem(updatedImageNode, ListItem.ItemStatus.UPDATE));
        showList("Updated list", list);
        nodeService.update(result, false);
        
        Node result1 = nodeService.get(listNode.getMainData().getId());
        list = result1.getExtraList();
        showList("Updated result", list);
        assertEquals(4, list.size());
        assertEquals(updatedImageNode.getMainData().getTitle(), list.get(0).node.getMainData().getTitle());
        assertEquals(musicNode.getMainData().getTitle(), list.get(1).node.getMainData().getTitle());
        assertEquals(videoNode.getMainData().getTitle(), list.get(2).node.getMainData().getTitle());
        assertEquals(updatedImageNode.getMainData().getTitle(), list.get(3).node.getMainData().getTitle());
        assertEquals(4, nodeMapper.selectAll().size());
        
        // delete list
        //
        // nodes(2): name       lcount  lstatus
        //    [-]    listNode   0       alone
        //    [ ]    textNode   0       attachment
        //    [-]    imageNode  2 -> 0  attachment
        //    [=]    musicNode  1 -> 0  alone
        //    [=]    videoNode  1 -> 0  alone
        nodeService.remove(listNode.getMainData().getId());
        assertEquals(2, nodeMapper.selectAll().size());
    }

    private Node newNode(String type, String title, ExtraData extraData) {
        Node node = new NodeDTO(com.my.list.domain.Node.defaultNode());
        MainData mainData = node.getMainData();
        mainData.setType(type);
        mainData.setTitle(title);
        if (extraData != null) node.setExtraData(extraData);
        return node;
    }
    private void showList(String msg, List<ListItem> list) {
        System.out.println("==== " + msg + " ====");
        AtomicInteger i = new AtomicInteger();
        list.forEach(item -> {
            String status;
            switch (item.status) {
                case EXIST:  status = "EXIST "; break;
                case NEW:    status = "NEW   "; break;
                case UPDATE: status = "UPDATE"; break;
                default: status = null; break;
            }
            System.out.println("[" + i.getAndIncrement() + "] " + status + " " + item.node.getMainData());
        });
    }
    
}
