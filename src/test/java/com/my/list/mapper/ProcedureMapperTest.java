package com.my.list.mapper;

import com.my.list.bean.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@SpringBootTest
class ProcedureMapperTest {

    @Autowired private ProcedureMapper procedureMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private NodeMapper nodeMapper;
    @Autowired private TextMapper textMapper;
    @Autowired private ImageMapper imageMapper;
    @Autowired private MusicMapper musicMapper;
    @Autowired private VideoMapper videoMapper;

    private static User user = new User();
    private static Text text = new Text();
    private static Image image = new Image();
    private static Music music = new Music();
    private static Video video = new Video();
    
    @BeforeAll
    static void beforeAll() {
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
    }

    @Test
    void test() {
        // clean_all
        procedureMapper.clean_all();

        // add_user
        procedureMapper.add_user(user);
        assertEquals(userMapper.selectAll().size(), 1);

        // check_user
        assertNotNull(procedureMapper.check_user(user.getName(), user.getPass()));
        assertNull(procedureMapper.check_user(user.getName(), "123456"));
        assertNull(procedureMapper.check_user("TestUser2", user.getPass()));

        // add_node
        procedureMapper.add_node(newNode("list", "Test Node"));
        assertEquals(nodeMapper.selectAll().size(), 1);

        // add_text
        procedureMapper.add_text(newNode("text", "Test Text"), text);
        assertEquals(nodeMapper.selectAll().size(), 2);
        assertEquals(textMapper.selectAll().size(), 1);

        // add_image
        procedureMapper.add_image(newNode("image", "Test Image"), image);
        assertEquals(nodeMapper.selectAll().size(), 3);
        assertEquals(imageMapper.selectAll().size(), 1);

        // add_music
        procedureMapper.add_music(newNode("music", "Test Music"), music);
        assertEquals(nodeMapper.selectAll().size(), 4);
        assertEquals(musicMapper.selectAll().size(), 1);

        // add_video
        procedureMapper.add_video(newNode("video", "Test Video"), video);
        assertEquals(nodeMapper.selectAll().size(), 5);
        assertEquals(videoMapper.selectAll().size(), 1);
        
        // show_all
        System.out.println(userMapper.selectAll());
        System.out.println(nodeMapper.selectAll());
        System.out.println(textMapper.selectAll());
        System.out.println(imageMapper.selectAll());
        System.out.println(musicMapper.selectAll());
        System.out.println(videoMapper.selectAll());
    }
    
    private Node newNode(String type, String title) {
        Node node = new Node();
        node.setUser(user.getId());
        node.setType(type);
        node.setTitle(title);
        return node;
    }

}
