package com.my.list.service;

import com.my.list.domain.NodeMapper;
import com.my.list.domain.ProcedureMapper;
import com.my.list.domain.User;
import com.my.list.dto.ExtraNode;
import com.my.list.dto.NodeDTO;
import com.my.list.dto.SingleNode;
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
    }

    @Test
    void textService() {
        ExtraNodeService extraNodeService = userService.getUserContext(token).extraNodeService;
        
        // addNode
        ExtraNode extraNode = newNode(Text.TYPE_NAME, "Text Node", text);
        SingleNode singleNode = extraNode.getSingleNode();
        extraNodeService.add(extraNode);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, textMapper.selectAll().size());

        // getNode
        ExtraNode result = extraNodeService.get(singleNode.getId());
        assertEquals(singleNode.getType(), result.getSingleNode().getType());
        assertArrayEquals(extraNode.getExtraData().keySet().toArray(), result.getExtraData().keySet().toArray());
        assertArrayEquals(extraNode.getExtraData().values().toArray(), result.getExtraData().values().toArray());

        // updateNode
        singleNode.setComment("This is comment.");
        extraNode.getExtraData().put("text_content", "This is text content.");
        extraNodeService.update(extraNode);
        result = extraNodeService.get(singleNode.getId());
        assertEquals(singleNode.getComment(), result.getSingleNode().getComment());
        assertEquals(ExtraData.parse(Text.class, extraNode.getExtraData()).getContent(), ExtraData.parse(Text.class, result.getExtraData()).getContent());

        // removeNode
        extraNodeService.remove(singleNode.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, textMapper.selectAll().size());
    }

    @Test
    void imageService() {
        ExtraNodeService extraNodeService = userService.getUserContext(token).extraNodeService;
        
        // addNode
        ExtraNode extraNode = newNode(Image.TYPE_NAME, "Image Node", image);
        SingleNode singleNode = extraNode.getSingleNode();
        extraNodeService.add(extraNode);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, imageMapper.selectAll().size());

        // getNode
        ExtraNode result = extraNodeService.get(singleNode.getId());
        assertEquals(singleNode.getType(), result.getSingleNode().getType());
        assertArrayEquals(extraNode.getExtraData().keySet().toArray(), result.getExtraData().keySet().toArray());
        assertArrayEquals(extraNode.getExtraData().values().toArray(), result.getExtraData().values().toArray());

        // updateNode
        singleNode.setComment("This is comment.");
        extraNode.getExtraData().put("image_description", "This is image description.");
        extraNodeService.update(extraNode);
        result = extraNodeService.get(singleNode.getId());
        assertEquals(singleNode.getComment(), result.getSingleNode().getComment());
        assertEquals(ExtraData.parse(Image.class, extraNode.getExtraData()).getDescription(), ExtraData.parse(Image.class, result.getExtraData()).getDescription());

        // removeNode
        extraNodeService.remove(singleNode.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, imageMapper.selectAll().size());
    }

    @Test
    void musicService() {
        ExtraNodeService extraNodeService = userService.getUserContext(token).extraNodeService;
        
        // addNode
        ExtraNode extraNode = newNode(Music.TYPE_NAME, "Music Node", music);
        SingleNode singleNode = extraNode.getSingleNode();
        extraNodeService.add(extraNode);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, musicMapper.selectAll().size());

        // getNode
        ExtraNode result = extraNodeService.get(singleNode.getId());
        assertEquals(singleNode.getType(), result.getSingleNode().getType());
        assertArrayEquals(extraNode.getExtraData().keySet().toArray(), result.getExtraData().keySet().toArray());
        assertArrayEquals(extraNode.getExtraData().values().toArray(), result.getExtraData().values().toArray());

        // updateNode
        singleNode.setComment("This is comment.");
        extraNode.getExtraData().put("music_format", "mp3");
        extraNodeService.update(extraNode);
        result = extraNodeService.get(singleNode.getId());
        assertEquals(singleNode.getComment(), result.getSingleNode().getComment());
        assertEquals(ExtraData.parse(Music.class, extraNode.getExtraData()).getFormat(), ExtraData.parse(Music.class, result.getExtraData()).getFormat());

        // removeNode
        extraNodeService.remove(singleNode.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, musicMapper.selectAll().size());
    }

    @Test
    void videoService() {
        ExtraNodeService extraNodeService = userService.getUserContext(token).extraNodeService;
        
        // addNode
        ExtraNode extraNode = newNode(Video.TYPE_NAME, "Video Node", video);
        SingleNode singleNode = extraNode.getSingleNode();
        extraNodeService.add(extraNode);
        assertEquals(1, nodeMapper.selectAll().size());
        assertEquals(1, videoMapper.selectAll().size());

        // getNode
        ExtraNode result = extraNodeService.get(singleNode.getId());
        assertEquals(singleNode.getType(), result.getSingleNode().getType());
        assertArrayEquals(extraNode.getExtraData().keySet().toArray(), result.getExtraData().keySet().toArray());
        assertArrayEquals(extraNode.getExtraData().values().toArray(), result.getExtraData().values().toArray());

        // updateNode
        singleNode.setComment("This is comment.");
        extraNode.getExtraData().put("video_format", "avi");
        extraNodeService.update(extraNode);
        result = extraNodeService.get(singleNode.getId());
        assertEquals(singleNode.getComment(), result.getSingleNode().getComment());
        assertEquals(ExtraData.parse(Video.class, extraNode.getExtraData()).getFormat(), ExtraData.parse(Video.class, result.getExtraData()).getFormat());

        // removeNode
        extraNodeService.remove(singleNode.getId());
        assertEquals(0, nodeMapper.selectAll().size());
        assertEquals(0, videoMapper.selectAll().size());
    }

    private ExtraNode newNode(String type, String title, ExtraData extraData) {
        ExtraNode extraNode = new NodeDTO();
        SingleNode singleNode = extraNode.getSingleNode();
        singleNode.setType(type);
        singleNode.setTitle(title);
        if (extraData != null) extraNode.setExtraData(extraData.toMap());
        return extraNode;
    }
    
}
