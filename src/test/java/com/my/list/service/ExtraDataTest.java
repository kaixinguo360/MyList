package com.my.list.service;

import com.my.list.TestUtil;
import com.my.list.domain.ExtraData;
import com.my.list.domain.MainData;
import com.my.list.domain.NodeMapper;
import com.my.list.domain.User;
import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.service.data.NodeService;
import com.my.list.type.image.Image;
import com.my.list.type.image.ImageConfig;
import com.my.list.type.image.ImageMapper;
import com.my.list.type.music.Music;
import com.my.list.type.music.MusicConfig;
import com.my.list.type.music.MusicMapper;
import com.my.list.type.text.Text;
import com.my.list.type.text.TextConfig;
import com.my.list.type.text.TextMapper;
import com.my.list.type.video.Video;
import com.my.list.type.video.VideoConfig;
import com.my.list.type.video.VideoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ExtraDataTest {

    @Autowired private TestUtil testUtil;
    @Autowired private NodeMapper nodeMapper;
    @Autowired private TextMapper textMapper;
    @Autowired private ImageMapper imageMapper;
    @Autowired private MusicMapper musicMapper;
    @Autowired private VideoMapper videoMapper;
    @Autowired private UserService userService;
    
    private String token;

    private static Text text = new Text();
    private static Image image = new Image();
    private static Music music = new Music();
    private static Video video = new Video();

    @BeforeEach
    void beforeAll() {
        User user = new User();
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
    }

    @Test
    void textDataTest() {
        NodeService nodeService = userService.getUserContext(token).nodeService;
        
        // addNode
        Node node = newNode(TextConfig.TYPE_NAME, "Text Node", text);
        MainData mainData = node.getMainData();
        nodeService.add(node);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, textMapper.selectAll().size());

        // getNode
        Node result = nodeService.get(mainData.getId());
        assertEquals(mainData.getType(), result.getMainData().getType());
        assertArrayEquals(node.getExtraData().toMap().keySet().toArray(), result.getExtraData().toMap().keySet().toArray());
        assertArrayEquals(node.getExtraData().toMap().values().toArray(), result.getExtraData().toMap().values().toArray());

        // updateNode
        mainData.setComment("This is comment.");
        node.getExtraData(Text.class).setContent("This is text content.");
        nodeService.update(node, false);
        result = nodeService.get(mainData.getId());
        assertEquals(mainData.getComment(), result.getMainData().getComment());
        assertEquals(node.getExtraData(Text.class).getContent(), result.getExtraData(Text.class).getContent());

        // removeNode
        nodeService.remove(mainData.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, textMapper.selectAll().size());
    }

    @Test
    void imageDataTest() {
        NodeService nodeService = userService.getUserContext(token).nodeService;
        
        // addNode
        Node node = newNode(ImageConfig.TYPE_NAME, "Image Node", image);
        MainData mainData = node.getMainData();
        nodeService.add(node);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, imageMapper.selectAll().size());

        // getNode
        Node result = nodeService.get(mainData.getId());
        assertEquals(mainData.getType(), result.getMainData().getType());
        assertArrayEquals(node.getExtraData().toMap().keySet().toArray(), result.getExtraData().toMap().keySet().toArray());
        assertArrayEquals(node.getExtraData().toMap().values().toArray(), result.getExtraData().toMap().values().toArray());

        // updateNode
        mainData.setComment("This is comment.");
        node.getExtraData(Image.class).setGallery("This is image description.");
        nodeService.update(node, false);
        result = nodeService.get(mainData.getId());
        assertEquals(mainData.getComment(), result.getMainData().getComment());
        assertEquals(node.getExtraData(Image.class).getGallery(), result.getExtraData(Image.class).getGallery());

        // removeNode
        nodeService.remove(mainData.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, imageMapper.selectAll().size());
    }

    @Test
    void musicDataTest() {
        NodeService nodeService = userService.getUserContext(token).nodeService;
        
        // addNode
        Node node = newNode(MusicConfig.TYPE_NAME, "Music Node", music);
        MainData mainData = node.getMainData();
        nodeService.add(node);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, musicMapper.selectAll().size());

        // getNode
        Node result = nodeService.get(mainData.getId());
        assertEquals(mainData.getType(), result.getMainData().getType());
        assertArrayEquals(node.getExtraData().toMap().keySet().toArray(), result.getExtraData().toMap().keySet().toArray());
        assertArrayEquals(node.getExtraData().toMap().values().toArray(), result.getExtraData().toMap().values().toArray());

        // updateNode
        mainData.setComment("This is comment.");
        node.getExtraData(Music.class).setFormat("mp3");
        nodeService.update(node, false);
        result = nodeService.get(mainData.getId());
        assertEquals(mainData.getComment(), result.getMainData().getComment());
        assertEquals(node.getExtraData(Music.class).getFormat(), result.getExtraData(Music.class).getFormat());

        // removeNode
        nodeService.remove(mainData.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, musicMapper.selectAll().size());
    }

    @Test
    void videoDataTest() {
        NodeService nodeService = userService.getUserContext(token).nodeService;
        
        // addNode
        Node node = newNode(VideoConfig.TYPE_NAME, "Video Node", video);
        MainData mainData = node.getMainData();
        nodeService.add(node);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, videoMapper.selectAll().size());

        // getNode
        Node result = nodeService.get(mainData.getId());
        assertEquals(mainData.getType(), result.getMainData().getType());
        assertArrayEquals(node.getExtraData().toMap().keySet().toArray(), result.getExtraData().toMap().keySet().toArray());
        assertArrayEquals(node.getExtraData().toMap().values().toArray(), result.getExtraData().toMap().values().toArray());

        // updateNode
        mainData.setComment("This is comment.");
        node.getExtraData(Video.class).setFormat("avi");
        nodeService.update(node, false);
        result = nodeService.get(mainData.getId());
        assertEquals(mainData.getComment(), result.getMainData().getComment());
        assertEquals(node.getExtraData(Video.class).getFormat(), result.getExtraData(Video.class).getFormat());

        // removeNode
        nodeService.remove(mainData.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, videoMapper.selectAll().size());
    }

    private Node newNode(String type, String title, ExtraData extraData) {
        Node node = new NodeDTO(com.my.list.domain.Node.Companion.defaultNode());
        MainData mainData = node.getMainData();
        mainData.setType(type);
        mainData.setTitle(title);
        if (extraData != null) node.setExtraData(extraData);
        return node;
    }
    
}
