package com.my.list.service;

import com.my.list.domain.*;
import com.my.list.dto.Node;
import com.my.list.type.ExtraData;
import com.my.list.type.image.Image;
import com.my.list.type.image.ImageMapper;
import com.my.list.type.music.Music;
import com.my.list.type.music.MusicMapper;
import com.my.list.type.text.Text;
import com.my.list.type.text.TextMapper;
import com.my.list.type.video.Video;
import com.my.list.type.video.VideoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ExtraNodeServiceTest {

    @Autowired private ProcedureMapper procedureMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private NodeMapper nodeMapper;
    @Autowired private TextMapper textMapper;
    @Autowired private ImageMapper imageMapper;
    @Autowired private MusicMapper musicMapper;
    @Autowired private VideoMapper videoMapper;
    @Autowired private PartMapper partMapper;

    @Autowired private ExtraNodeService extraNodeService;

    private static User user = new User();
    private static Text text = new Text();
    private static Image image = new Image();
    private static Music music = new Music();
    private static Video video = new Video();

    @BeforeEach
    void beforeAll() {
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
    }

    @Test
    void singleExtraNodeService() {
        // addNode
        Node node = newNode("node", "Single Node", null);
        extraNodeService.addNode(node);
        assertEquals(1, nodeMapper.selectAll().size());

        // getNode
        Node node1 = extraNodeService.getNode(node.getId());
        assertEquals(node.getType(), node1.getType());
        assertArrayEquals(node.getExtraData().keySet().toArray(), node1.getExtraData().keySet().toArray());
        assertArrayEquals(node.getExtraData().values().toArray(), node1.getExtraData().values().toArray());

        // updateNode
        node.setComment("This is comment.");
        extraNodeService.updateNode(node);
        assertEquals(node.getComment(), extraNodeService.getNode(node.getId()).getComment());

        // removeNode
        extraNodeService.removeNode(node.getId());
        assertEquals(0, nodeMapper.selectAll().size());
    }

    @Test
    void textService() {
        // addNode
        Node node = newNode(Text.TYPE_NAME, "Text Node", text);
        extraNodeService.addNode(node);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, textMapper.selectAll().size());

        // getNode
        Node result = extraNodeService.getNode(node.getId());
        assertEquals(node.getType(), result.getType());
        assertArrayEquals(node.getExtraData().keySet().toArray(), result.getExtraData().keySet().toArray());
        assertArrayEquals(node.getExtraData().values().toArray(), result.getExtraData().values().toArray());

        // updateNode
        node.setComment("This is comment.");
        node.getExtraData().put("text_content", "This is text content.");
        extraNodeService.updateNode(node);
        result = extraNodeService.getNode(node.getId());
        assertEquals(node.getComment(), result.getComment());
        assertEquals(ExtraData.parse(Text.class, node.getExtraData()).getContent(), ExtraData.parse(Text.class, result.getExtraData()).getContent());

        // removeNode
        extraNodeService.removeNode(node.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, textMapper.selectAll().size());
    }

    @Test
    void imageService() {
        // addNode
        Node node = newNode(Image.TYPE_NAME, "Image Node", image);
        extraNodeService.addNode(node);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, imageMapper.selectAll().size());

        // getNode
        Node result = extraNodeService.getNode(node.getId());
        assertEquals(node.getType(), result.getType());
        assertArrayEquals(node.getExtraData().keySet().toArray(), result.getExtraData().keySet().toArray());
        assertArrayEquals(node.getExtraData().values().toArray(), result.getExtraData().values().toArray());

        // updateNode
        node.setComment("This is comment.");
        node.getExtraData().put("image_description", "This is image description.");
        extraNodeService.updateNode(node);
        result = extraNodeService.getNode(node.getId());
        assertEquals(node.getComment(), result.getComment());
        assertEquals(ExtraData.parse(Image.class, node.getExtraData()).getDescription(), ExtraData.parse(Image.class, result.getExtraData()).getDescription());

        // removeNode
        extraNodeService.removeNode(node.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, imageMapper.selectAll().size());
    }

    @Test
    void musicService() {
        // addNode
        Node node = newNode(Music.TYPE_NAME, "Music Node", music);
        extraNodeService.addNode(node);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, musicMapper.selectAll().size());

        // getNode
        Node result = extraNodeService.getNode(node.getId());
        assertEquals(node.getType(), result.getType());
        assertArrayEquals(node.getExtraData().keySet().toArray(), result.getExtraData().keySet().toArray());
        assertArrayEquals(node.getExtraData().values().toArray(), result.getExtraData().values().toArray());

        // updateNode
        node.setComment("This is comment.");
        node.getExtraData().put("music_format", "mp3");
        extraNodeService.updateNode(node);
        result = extraNodeService.getNode(node.getId());
        assertEquals(node.getComment(), result.getComment());
        assertEquals(ExtraData.parse(Music.class, node.getExtraData()).getFormat(), ExtraData.parse(Music.class, result.getExtraData()).getFormat());

        // removeNode
        extraNodeService.removeNode(node.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, musicMapper.selectAll().size());
    }

    @Test
    void videoService() {
        // addNode
        Node node = newNode(Video.TYPE_NAME, "Video Node", video);
        extraNodeService.addNode(node);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, videoMapper.selectAll().size());

        // getNode
        Node result = extraNodeService.getNode(node.getId());
        assertEquals(node.getType(), result.getType());
        assertArrayEquals(node.getExtraData().keySet().toArray(), result.getExtraData().keySet().toArray());
        assertArrayEquals(node.getExtraData().values().toArray(), result.getExtraData().values().toArray());

        // updateNode
        node.setComment("This is comment.");
        node.getExtraData().put("video_format", "avi");
        extraNodeService.updateNode(node);
        result = extraNodeService.getNode(node.getId());
        assertEquals(node.getComment(), result.getComment());
        assertEquals(ExtraData.parse(Video.class, node.getExtraData()).getFormat(), ExtraData.parse(Video.class, result.getExtraData()).getFormat());

        // removeNode
        extraNodeService.removeNode(node.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, videoMapper.selectAll().size());
    }

    private Node newNode(String type, String title, ExtraData extraData) {
        Node node = new Node();
        node.setUser(user.getId());
        node.setType(type);
        node.setTitle(title);
        if (extraData != null) node.setExtraData(extraData.toMap());
        return node;
    }

    private void showAll() {
        System.out.println(userMapper.selectAll());
        System.out.println(nodeMapper.selectAll());
        System.out.println(textMapper.selectAll());
        System.out.println(imageMapper.selectAll());
        System.out.println(musicMapper.selectAll());
        System.out.println(videoMapper.selectAll());
        System.out.println(partMapper.selectAll());
    }
    
}
