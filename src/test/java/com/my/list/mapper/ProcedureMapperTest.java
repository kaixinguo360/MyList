package com.my.list.mapper;

import com.my.list.domain.*;
import com.my.list.type.image.Image;
import com.my.list.type.image.ImageMapper;
import com.my.list.type.music.Music;
import com.my.list.type.music.MusicMapper;
import com.my.list.type.text.Text;
import com.my.list.type.text.TextMapper;
import com.my.list.type.video.Video;
import com.my.list.type.video.VideoMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
    @Autowired private PartMapper partMapper;

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
        Node node = newNode("node", "Test Node");
        procedureMapper.add_node(node);
        assertEquals(nodeMapper.selectAll().size(), 1);

        // add_list
        Node list_node = newNode("list", "Test Node");
        procedureMapper.add_list(list_node);
        assertEquals(nodeMapper.selectAll().size(), 2);

        // add_text
        Node text_node = newNode("text", "Test Text");
        text_node.setLstatus("attachment");
        procedureMapper.add_text(text_node, text);
        assertEquals(nodeMapper.selectAll().size(), 3);
        assertEquals(textMapper.selectAll().size(), 1);

        // add_image
        Node image_node = newNode("image", "Test Image");
        procedureMapper.add_image(image_node, image);
        assertEquals(nodeMapper.selectAll().size(), 4);
        assertEquals(imageMapper.selectAll().size(), 1);

        // add_music
        Node music_node = newNode("music", "Test Music");
        procedureMapper.add_music(music_node, music);
        assertEquals(nodeMapper.selectAll().size(), 5);
        assertEquals(musicMapper.selectAll().size(), 1);

        // add_video
        Node video_node = newNode("video", "Test Video");
        procedureMapper.add_video(video_node, video);
        assertEquals(nodeMapper.selectAll().size(), 6);
        assertEquals(videoMapper.selectAll().size(), 1);
        
        // add_part
        procedureMapper.add_part(list_node.getId(), text_node.getId());
        procedureMapper.add_part(list_node.getId(), image_node.getId());
        procedureMapper.add_part(list_node.getId(), music_node.getId());
        procedureMapper.add_part(list_node.getId(), video_node.getId());
        assertEquals(partMapper.selectAll().size(), 4);
        
        // show_all
        showAll();
        
        // clean_list
        procedureMapper.clean_list(list_node.getId());
        assertEquals(partMapper.selectAll().size(), 0);

        // clean_nodes
        procedureMapper.clean_nodes();
        assertEquals(nodeMapper.selectAll().size(), 5);
        assertEquals(textMapper.selectAll().size(), 0);
        
        // delete_list
        image_node.setLstatus("attachment");
        music_node.setLstatus("attachment");
        video_node.setLstatus("attachment");
        nodeMapper.updateByPrimaryKey(image_node);
        nodeMapper.updateByPrimaryKey(music_node);
        nodeMapper.updateByPrimaryKey(video_node);
        procedureMapper.add_part(list_node.getId(), image_node.getId());
        procedureMapper.add_part(list_node.getId(), music_node.getId());
        procedureMapper.add_part(list_node.getId(), video_node.getId());
        assertEquals(partMapper.selectAll().size(), 3);
        procedureMapper.delete_list(list_node.getId());
        assertEquals(nodeMapper.selectAll().size(), 1);
        assertEquals(textMapper.selectAll().size(), 0);
        assertEquals(imageMapper.selectAll().size(), 0);
        assertEquals(musicMapper.selectAll().size(), 0);
        assertEquals(videoMapper.selectAll().size(), 0);
        assertEquals(partMapper.selectAll().size(), 0);
        
        // delete_user
        userMapper.deleteByPrimaryKey(user.getId());
        assertEquals(userMapper.selectAll().size(), 0);
        assertEquals(nodeMapper.selectAll().size(), 0);
    }
    
    private Node newNode(String type, String title) {
        Node node = new Node();
        node.setUser(user.getId());
        node.setType(type);
        node.setTitle(title);
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
