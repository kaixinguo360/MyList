package com.my.list.service;

import com.my.list.domain.*;
import com.my.list.dto.Node;
import com.my.list.type.ExtraValues;
import com.my.list.type.image.Image;
import com.my.list.type.image.ImageMapper;
import com.my.list.type.image.ImageService;
import com.my.list.type.music.Music;
import com.my.list.type.music.MusicMapper;
import com.my.list.type.music.MusicService;
import com.my.list.type.text.Text;
import com.my.list.type.text.TextMapper;
import com.my.list.type.text.TextService;
import com.my.list.type.video.Video;
import com.my.list.type.video.VideoMapper;
import com.my.list.type.video.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class NodeServiceTest {

    @Autowired private ProcedureMapper procedureMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private NodeMapper nodeMapper;
    @Autowired private TextMapper textMapper;
    @Autowired private ImageMapper imageMapper;
    @Autowired private MusicMapper musicMapper;
    @Autowired private VideoMapper videoMapper;
    @Autowired private PartMapper partMapper;

    @Autowired private NodeService nodeService;
    @Autowired private TextService textService;
    @Autowired private ImageService imageService;
    @Autowired private MusicService musicService;
    @Autowired private VideoService videoService;

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
    void singleNodeService() {
        // addNode
        Node node = newNode("node", "Single Node", null);
        nodeService.addNode(node);
        assertEquals(1, nodeMapper.selectAll().size());

        // getNode
        Node node1 = nodeService.getNode(node.getId());
        assertEquals(node.getType(), node1.getType());
        assertArrayEquals(node.getExtraValues().keySet().toArray(), node1.getExtraValues().keySet().toArray());
        assertArrayEquals(node.getExtraValues().values().toArray(), node1.getExtraValues().values().toArray());

        // updateNode
        node.setComment("This is comment.");
        nodeService.updateNode(node);
        assertEquals(node.getComment(), nodeService.getNode(node.getId()).getComment());

        // removeNode
        nodeService.removeNode(node.getId());
        assertEquals(0, nodeMapper.selectAll().size());
    }

    @Test
    void textService() {
        // addNode
        Node node = newNode(textService.typeName, "Text Node", text);
        nodeService.addNode(node);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, textMapper.selectAll().size());

        // getNode
        Node result = nodeService.getNode(node.getId());
        assertEquals(node.getType(), result.getType());
        assertArrayEquals(node.getExtraValues().keySet().toArray(), result.getExtraValues().keySet().toArray());
        assertArrayEquals(node.getExtraValues().values().toArray(), result.getExtraValues().values().toArray());

        // updateNode
        node.setComment("This is comment.");
        node.getExtraValues().put("text_content", "This is text content.");
        nodeService.updateNode(node);
        result = nodeService.getNode(node.getId());
        assertEquals(node.getComment(), result.getComment());
        assertEquals(textService.parse(node.getExtraValues()).getContent(), textService.parse(result.getExtraValues()).getContent());

        // removeNode
        nodeService.removeNode(node.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, textMapper.selectAll().size());
    }

    @Test
    void imageService() {
        // addNode
        Node node = newNode(imageService.typeName, "Image Node", image);
        nodeService.addNode(node);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, imageMapper.selectAll().size());

        // getNode
        Node result = nodeService.getNode(node.getId());
        assertEquals(node.getType(), result.getType());
        assertArrayEquals(node.getExtraValues().keySet().toArray(), result.getExtraValues().keySet().toArray());
        assertArrayEquals(node.getExtraValues().values().toArray(), result.getExtraValues().values().toArray());

        // updateNode
        node.setComment("This is comment.");
        node.getExtraValues().put("image_description", "This is image description.");
        nodeService.updateNode(node);
        result = nodeService.getNode(node.getId());
        assertEquals(node.getComment(), result.getComment());
        assertEquals(imageService.parse(node.getExtraValues()).getDescription(), imageService.parse(result.getExtraValues()).getDescription());

        // removeNode
        nodeService.removeNode(node.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, imageMapper.selectAll().size());
    }

    @Test
    void musicService() {
        // addNode
        Node node = newNode(musicService.typeName, "Music Node", music);
        nodeService.addNode(node);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, musicMapper.selectAll().size());

        // getNode
        Node result = nodeService.getNode(node.getId());
        assertEquals(node.getType(), result.getType());
        assertArrayEquals(node.getExtraValues().keySet().toArray(), result.getExtraValues().keySet().toArray());
        assertArrayEquals(node.getExtraValues().values().toArray(), result.getExtraValues().values().toArray());

        // updateNode
        node.setComment("This is comment.");
        node.getExtraValues().put("music_format", "mp3");
        nodeService.updateNode(node);
        result = nodeService.getNode(node.getId());
        assertEquals(node.getComment(), result.getComment());
        assertEquals(musicService.parse(node.getExtraValues()).getFormat(), musicService.parse(result.getExtraValues()).getFormat());

        // removeNode
        nodeService.removeNode(node.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, musicMapper.selectAll().size());
    }

    @Test
    void videoService() {
        // addNode
        Node node = newNode(videoService.typeName, "Video Node", video);
        nodeService.addNode(node);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, videoMapper.selectAll().size());

        // getNode
        Node result = nodeService.getNode(node.getId());
        assertEquals(node.getType(), result.getType());
        assertArrayEquals(node.getExtraValues().keySet().toArray(), result.getExtraValues().keySet().toArray());
        assertArrayEquals(node.getExtraValues().values().toArray(), result.getExtraValues().values().toArray());

        // updateNode
        node.setComment("This is comment.");
        node.getExtraValues().put("video_format", "avi");
        nodeService.updateNode(node);
        result = nodeService.getNode(node.getId());
        assertEquals(node.getComment(), result.getComment());
        assertEquals(videoService.parse(node.getExtraValues()).getFormat(), videoService.parse(result.getExtraValues()).getFormat());

        // removeNode
        nodeService.removeNode(node.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, videoMapper.selectAll().size());
    }

    private Node newNode(String type, String title, ExtraValues extraValues) {
        Node node = new Node();
        node.setUser(user.getId());
        node.setType(type);
        node.setTitle(title);
        if (extraValues != null) node.setExtraValues(extraValues.toMap());
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
