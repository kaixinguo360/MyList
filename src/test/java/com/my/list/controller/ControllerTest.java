package com.my.list.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.Constants;
import com.my.list.TestUtil;
import com.my.list.aop.SimpleResponseEntity;
import com.my.list.entity.*;
import com.my.list.entity.filter.Filter;
import com.my.list.entity.filter.Permission;
import com.my.list.entity.filter.Sort;
import com.my.list.entity.filter.Tag;
import com.my.list.service.UserService;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest
public class ControllerTest {
    
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext context;
    private MockMvc mvc;
    private String token;
    private Node textNode, imageNode, musicNode, videoNode, listNode, tagNode;

    @Autowired private TestUtil testUtil;
    @Autowired private UserService userService;

    private static User user = new User();
    private static Text text = new Text();
    private static Image image = new Image();
    private static Music music = new Music();
    private static Video video = new Video();

    @BeforeEach
    void beforeAll() {
        user.setName("TestUser");
        user.setPass("*6A7A490FB9DC8C33C2B025A91737077A7E9CC5E5");
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
        
        // setup MockMvc
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void controllerTest() throws Exception {
        adminTest();
        tokenTest();
        nodeTest();
        listTest();
        searchTest();
    }
    public void adminTest() throws Exception {
        // get admin token
        String adminToken = assertResult(
            mvc.perform(MockMvcRequestBuilders
                .get("/api/token/admin?pass=1234567")
            ), String.class);
        
        // user - post
        User user1 = assertResult(mvc.perform(MockMvcRequestBuilders.post("/api/user").header(Constants.AUTHORIZATION, adminToken).contentType(MediaType.APPLICATION_JSON)
            .content(toJson(newUser("User1", "12345")))
        ), User.class);
        User user2 = assertResult(mvc.perform(MockMvcRequestBuilders.post("/api/user").header(Constants.AUTHORIZATION, adminToken).contentType(MediaType.APPLICATION_JSON)
            .content(toJson(user))
        ), User.class);

        // user - getAll
        assertEquals(2,
            assertResult(mvc.perform(MockMvcRequestBuilders
                .get("/api/user")
                .header(Constants.AUTHORIZATION, adminToken)
            ), List.class).size()
        );
        // user - get
        assertEquals(user1.getName(),
            assertResult(mvc.perform(MockMvcRequestBuilders
                .get("/api/user/" + user1.getId())
                .header(Constants.AUTHORIZATION, adminToken)
            ), User.class).getName()
        );

        // user - update
        user1.setPass("1234567");
        assertEquals(user1.getName(),
            assertResult(mvc.perform(MockMvcRequestBuilders
                .put("/api/user/")
                .header(Constants.AUTHORIZATION, adminToken).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user1))
            ), User.class).getName()
        );

        // user - token
        String token1 = assertResult(
            mvc.perform(MockMvcRequestBuilders
                .get("/api/token?name=User1&pass=1234567")
            ), TokenController.OutputWarp.class).getToken();
        mvc.perform(MockMvcRequestBuilders
            .get("/api/user/" + user1.getId())
            .header(Constants.AUTHORIZATION, token1)
        ).andExpect(MockMvcResultMatchers.status().is4xxClientError());

        // user - delete
        mvc.perform(MockMvcRequestBuilders
            .delete("/api/user/" + user1.getId())
            .header(Constants.AUTHORIZATION, adminToken)
        ).andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(MockMvcRequestBuilders
            .get("/api/token?name=User1&pass=1234567")
        ).andExpect(MockMvcResultMatchers.status().is4xxClientError());

        // delete admin token
        mvc.perform(MockMvcRequestBuilders
            .delete("/api/token/admin")
            .header(Constants.AUTHORIZATION, adminToken)
        ).andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(MockMvcRequestBuilders
            .get("/api/user")
            .header(Constants.AUTHORIZATION, adminToken)
        ).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
    public void tokenTest() throws Exception {

        // token - get
        token = assertResult(
            mvc.perform(MockMvcRequestBuilders
                .get("/api/token?name=TestUser&pass=*6A7A490FB9DC8C33C2B025A91737077A7E9CC5E5")
            ), TokenController.OutputWarp.class).getToken();
        String tmpToken = assertResult(
            mvc.perform(MockMvcRequestBuilders
                .get("/api/token?name=TestUser&pass=*6A7A490FB9DC8C33C2B025A91737077A7E9CC5E5")
            ), TokenController.OutputWarp.class).getToken();

        // token - delete
        assertSuccess(
            mvc.perform(MockMvcRequestBuilders
                .delete("/api/token")
                .header(Constants.AUTHORIZATION, tmpToken)
            ));
    }
    public void nodeTest() throws Exception {

        // node - post
        textNode = assertNode(mvc.perform(MockMvcRequestBuilders.post("/api/node").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            .content(toJson(newNode(TextConfig.TYPE_NAME, "Text Node", text)))
        ));
        imageNode = assertNode(mvc.perform(MockMvcRequestBuilders.post("/api/node").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            .content(toJson(newNode(ImageConfig.TYPE_NAME, "Image Node", image)))
        ));
        musicNode = assertNode(mvc.perform(MockMvcRequestBuilders.post("/api/node").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            .content(toJson(newNode(MusicConfig.TYPE_NAME, "Music Node", music)))
        ));
        videoNode = assertNode(mvc.perform(MockMvcRequestBuilders.post("/api/node").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            .content(toJson(newNode(VideoConfig.TYPE_NAME, "Video Node", video)))
        ));

        // node - get
        assertEquals(textNode.getMainData().getTitle(),
            assertNode(mvc.perform(MockMvcRequestBuilders
                .get("/api/node/" + textNode.getMainData().getId())
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            )).getMainData().getTitle()
        );
        assertEquals(imageNode.getMainData().getTitle(),
            assertNode(mvc.perform(MockMvcRequestBuilders
                .get("/api/node/" + imageNode.getMainData().getId())
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            )).getMainData().getTitle()
        );
        assertEquals(musicNode.getMainData().getTitle(),
            assertNode(mvc.perform(MockMvcRequestBuilders
                .get("/api/node/" + musicNode.getMainData().getId())
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            )).getMainData().getTitle()
        );
        assertEquals(videoNode.getMainData().getTitle(),
            assertNode(mvc.perform(MockMvcRequestBuilders
                .get("/api/node/" + videoNode.getMainData().getId())
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            )).getMainData().getTitle()
        );

        // node - update
        textNode.getExtraData(Text.class).setContent("New Text Content");
        assertEquals(textNode.getExtraData(Text.class).getContent(),
            assertNode(mvc.perform(MockMvcRequestBuilders
                .put("/api/node/")
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(textNode))
            )).getExtraData(Text.class).getContent()
        );
        imageNode.getExtraData(Image.class).setGallery("New Image Description");
        assertEquals(imageNode.getExtraData(Image.class).getGallery(),
            assertNode(mvc.perform(MockMvcRequestBuilders
                .put("/api/node/")
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(imageNode))
            )).getExtraData(Image.class).getGallery()
        );
        musicNode.getExtraData(Music.class).setFormat("newMusicFormat");
        assertEquals(musicNode.getExtraData(Music.class).getFormat(),
            assertNode(mvc.perform(MockMvcRequestBuilders
                .put("/api/node/")
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(musicNode))
            )).getExtraData(Music.class).getFormat()
        );
        videoNode.getExtraData(Video.class).setFormat("newVideoFormat");
        assertEquals(videoNode.getExtraData(Video.class).getFormat(),
            assertNode(mvc.perform(MockMvcRequestBuilders
                .put("/api/node/")
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(videoNode))
            )).getExtraData(Video.class).getFormat()
        );
    }
    public void listTest() throws Exception {

        // ---- node - list ---- //
        Node newListNode = newNode("list", "Test List", null);
        List<ListItem> newList = new ArrayList<>();
        newListNode.setExtraList(newList);

        newList.add(new ListItem(textNode, ListItem.ItemStatus.EXIST));
        newList.add(new ListItem(imageNode, ListItem.ItemStatus.EXIST));
        newList.add(new ListItem(musicNode, ListItem.ItemStatus.EXIST));
        newList.add(new ListItem(videoNode, ListItem.ItemStatus.EXIST));

        // post
        listNode = assertNode(mvc.perform(MockMvcRequestBuilders.post("/api/node").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(newListNode)))
        );
        showList("listNode", listNode.getExtraList());
        assertEquals(4, listNode.getExtraList().size());

        // put
        listNode.getExtraList().remove(3);
        assertEquals(3,
            assertNode(mvc.perform(MockMvcRequestBuilders
                .put("/api/node/")
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(listNode))
            )).getExtraList().size()
        );

        
        // ---- node - tag ---- //
        Node newTagNode = newNode("tag", "Test Tag", null);
        List<ListItem> newTag = new ArrayList<>();
        newTagNode.setExtraList(newTag);

        newTag.add(new ListItem(textNode, ListItem.ItemStatus.EXIST));
        newTag.add(new ListItem(imageNode, ListItem.ItemStatus.EXIST));
        newTag.add(new ListItem(musicNode, ListItem.ItemStatus.EXIST));
        newTag.add(new ListItem(videoNode, ListItem.ItemStatus.EXIST));

        // post
        tagNode = assertNode(mvc.perform(MockMvcRequestBuilders.post("/api/node").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(newTagNode)))
        );
        showList("tagNode", tagNode.getExtraList());
        assertEquals(4, tagNode.getExtraList().size());

        // put
        tagNode.getExtraList().remove(3);
        assertEquals(3,
            assertNode(mvc.perform(MockMvcRequestBuilders
                .put("/api/node/")
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(tagNode))
            )).getExtraList().size()
        );
    }
    public void searchTest() throws Exception {

        // search - getAll
        assertNodes("getAll", 6, assertList(
            mvc.perform(MockMvcRequestBuilders
                .get("/api/node/search").header(Constants.AUTHORIZATION, token)
            )));

        // search - query
        assertNodes("'Text Node'", 1, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/node/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new Filter()
                    .addCondition("content.node_title", "=", "'Text Node'")
                ))
            )));
        assertNodes("'%Text%'", 1, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/node/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new Filter()
                    .addCondition("content.node_title", "like", "'%Text%'")
                ))
            )));
        assertNodes("'%Text%', ctime asc", 1, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/node/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new Filter()
                    .addCondition("content.node_title", "like", "'%Text%'")
                    .addSort("node_ctime", Sort.Direction.ASC)
                ))
            )));

        assertNodes("permission=PRIVATE", 6, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/node/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new Filter()
                    .setPermission(Permission.PRIVATE)
                ))
            )));
        assertNodes("permission=PUBLIC", 0, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/node/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new Filter()
                    .setPermission(Permission.PUBLIC)
                ))
            )));

        assertNodes("orTag='Test Tag'", 3, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/node/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new Filter()
                    .addOrTag(new Tag("Test Tag"))
                ))
            )));
        assertNodes("andTag='%Tag%'", 3, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/node/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new Filter()
                    .addAndTag(new Tag("Tag", false))
                ))
            )));
        assertNodes("notTag='%Node%'", 3, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/node/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new Filter()
                    .addNotTag(new Tag("Tag", false))
                ))
            )));
        assertNodes("orTag='%Tag%', andTag='%Test%'", 3, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/node/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new Filter()
                    .addOrTag(new Tag("Tag", false))
                    .addAndTag(new Tag("Test", false))
                ))
            )));
        assertNodes("orTag='%Tag%', andTag='%Test%', notTag='Test Tag'", 0, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/node/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new Filter()
                    .addOrTag(new Tag("Tag", false))
                    .addAndTag(new Tag("Test", false))
                    .addNotTag(new Tag("Test Tag"))
                ))
            )));
    }

    private User newUser(String name, String pass) {
        User user = new User();
        user.setName(name);
        user.setPass(pass);
        user.setStatus("activated");
        user.setEmail(name + "@example.com");
        return user;
    }
    private Node newNode(String type, String title, ExtraData extraData) {
        Node node = new NodeImpl(MainData.defaultNode());
        MainData mainData = node.getMainData();
        mainData.setType(type);
        mainData.setTitle(title);
        if (extraData != null) node.setExtraData(extraData);
        return node;
    }

    private byte[] toJson(Node node) throws JsonProcessingException {
        NodeController.NodeOutputWrap output = new NodeController.NodeOutputWrap();
        output.node = node;
        return objectMapper.writeValueAsBytes(output);
    }
    private byte[] toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(object);
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
    private List<Node> assertList(ResultActions resultActions) throws Exception {
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<?, ?>> list = (List<LinkedHashMap<?, ?>>) assertResult(resultActions, List.class);
        return list.stream().map(map -> objectMapper.convertValue(map, Node.class)).collect(Collectors.toList());
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
    
    private Node assertNode(ResultActions resultActions) throws Exception {
        Object object = objectMapper.readValue(resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").hasJsonPath())
                .andReturn().getResponse().getContentAsString()
            , SimpleResponseEntity.class
        ).getResult();
        if (object instanceof Map) {
            @SuppressWarnings("unchecked") Map<String, Object> map = (Map<String, Object>) object;
            return objectMapper.convertValue(map.get("node"), Node.class);
        }
        throw new AssertionError();
    }
    private void assertSuccess(ResultActions resultActions) throws Exception {
        assertResult(resultActions, Object.class);
    }
    
    private <T> T assertResult(ResultActions resultActions, Class<T> clazz) throws Exception {
        Object object = objectMapper.readValue(resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").hasJsonPath())
                .andReturn().getResponse().getContentAsString()
            , SimpleResponseEntity.class
        ).getResult();
        return objectMapper.convertValue(object, clazz);
    }
}
