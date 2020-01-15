package com.my.list.service;

import com.my.list.domain.NodeMapper;
import com.my.list.domain.ProcedureMapper;
import com.my.list.domain.User;
import com.my.list.dto.ExtraNode;
import com.my.list.dto.ListItem;
import com.my.list.dto.NodeDTO;
import com.my.list.dto.SingleNode;
import com.my.list.type.ExtraData;
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

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ListServiceTest {

    @Autowired private ProcedureMapper procedureMapper;
    @Autowired private NodeMapper nodeMapper;
    @Autowired private UserService userService;

    private String token;

    private static ExtraNode textNode;
    private static ExtraNode imageNode;
    private static ExtraNode musicNode;
    private static ExtraNode videoNode;

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
        //
        // nodes(2): name       lcount  lstatus
        //    [ ]    listNode   0       alone
        //    [+]    textNode   0       attachment
        //    [+]    imageNode  0       attachment
        //    [ ]    musicNode  0       alone
        //    [ ]    videoNode  0       alone
        textNode = newNode(Text.TYPE_NAME, "Text Node", text);
        imageNode = newNode(Image.TYPE_NAME, "Image Node", image);
        musicNode = newNode(Music.TYPE_NAME, "Music Node", music);
        videoNode = newNode(Video.TYPE_NAME, "Video Node", video);
        textNode.getSingleNode().setLstatus("attachment");
        imageNode.getSingleNode().setLstatus("attachment");
        ExtraNodeService extraNodeService = userService.getUserContext(token).extraNodeService;
        extraNodeService.add(textNode);
        extraNodeService.add(imageNode);
    }
    
    @Test
    void listService() {
        ExtraNodeService extraNodeService = userService.getUserContext(token).extraNodeService;
        
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
        ExtraNode listNode = newNode("list", "List Node", null);
        List<ListItem> list = new ArrayList<>();
        list.add(new ListItem(textNode.getSingleNode()));
        list.add(new ListItem(imageNode.getSingleNode()));
        list.add(new ListItem(musicNode));
        listNode.setExtraList(list);
        extraNodeService.add(listNode);
        
        // get list
        ExtraNode result = extraNodeService.get(listNode.getSingleNode().getId());
        list = result.getExtraList();
        showList("Get result", list);
        assertEquals(3, list.size());
        assertEquals(textNode.getSingleNode().getTitle(), list.get(0).singleNode.getTitle());
        assertEquals(imageNode.getSingleNode().getTitle(), list.get(1).singleNode.getTitle());
        assertEquals(musicNode.getSingleNode().getTitle(), list.get(2).singleNode.getTitle());
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
        list.add(new ListItem(videoNode));
        list.add(new ListItem(imageNode.getSingleNode()));
        ExtraNode updatedImageNode = extraNodeService.get(list.get(3).singleNode.getId());
        updatedImageNode.getSingleNode().setTitle("Updated Image Node");
        list.set(3, new ListItem(updatedImageNode));
        showList("Updated list", list);
        extraNodeService.update(result);
        
        ExtraNode result1 = extraNodeService.get(listNode.getSingleNode().getId());
        list = result1.getExtraList();
        showList("Updated result", list);
        assertEquals(4, list.size());
        assertEquals(updatedImageNode.getSingleNode().getTitle(), list.get(0).singleNode.getTitle());
        assertEquals(musicNode.getSingleNode().getTitle(), list.get(1).singleNode.getTitle());
        assertEquals(videoNode.getSingleNode().getTitle(), list.get(2).singleNode.getTitle());
        assertEquals(updatedImageNode.getSingleNode().getTitle(), list.get(3).singleNode.getTitle());
        assertEquals(4, nodeMapper.selectAll().size());
        
        // delete list
        //
        // nodes(2): name       lcount  lstatus
        //    [-]    listNode   0       alone
        //    [ ]    textNode   0       attachment
        //    [-]    imageNode  2 -> 0  attachment
        //    [=]    musicNode  1 -> 0  alone
        //    [=]    videoNode  1 -> 0  alone
        extraNodeService.remove(listNode.getSingleNode().getId());
        assertEquals(2, nodeMapper.selectAll().size());
    }

    private ExtraNode newNode(String type, String title, ExtraData extraData) {
        ExtraNode extraNode = new NodeDTO();
        SingleNode singleNode = extraNode.getSingleNode();
        singleNode.setType(type);
        singleNode.setTitle(title);
        if (extraData != null) extraNode.setExtraData(extraData.toMap());
        return extraNode;
    }
    private void showList(String msg, List<ListItem> list) {
        System.out.println("==== " + msg + " ====");
        AtomicInteger i = new AtomicInteger();
        list.forEach(item -> {
            System.out.print("[" + i.getAndIncrement() + "] ");
            switch (item.itemStatus) {
                case EXIST:  System.out.println("EXIST  " + item.singleNode); break;
                case NEW:    System.out.println("NEW    " + item.extraNode); break;
                case UPDATE: System.out.println("UPDATE " + item.extraNode); break;
            }
        });
    }
    
}
