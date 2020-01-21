package com.my.list.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.controller.util.Constants;
import com.my.list.controller.util.SimpleResponseEntity;
import com.my.list.domain.ExtraData;
import com.my.list.domain.MainData;
import com.my.list.domain.ProcedureMapper;
import com.my.list.domain.User;
import com.my.list.dto.ListItem;
import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.service.search.Permission;
import com.my.list.service.search.Query;
import com.my.list.service.search.Sort;
import com.my.list.service.search.Tag;
import com.my.list.type.image.Image;
import com.my.list.type.music.Music;
import com.my.list.type.text.Text;
import com.my.list.type.video.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest
public class ControllerTest {
    
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext context;
    private MockMvc mvc;
    private String token;
    private Node textNode, imageNode, musicNode, videoNode, listNode, tagNode;

    @Autowired private ProcedureMapper procedureMapper;

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
        
        // setup MockMvc
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void test() throws Exception {
        tokenTest();
        nodeTest();
        listTest();
        searchTest();
    }
    public void tokenTest() throws Exception {
        
        // token - get
        token = assertResult(
            mvc.perform(MockMvcRequestBuilders
                .get("/api/token?name=TestUser&pass=*6A7A490FB9DC8C33C2B025A91737077A7E9CC5E5")
            ), String.class);
        String tmpToken = assertResult(
            mvc.perform(MockMvcRequestBuilders
                .get("/api/token?name=TestUser&pass=*6A7A490FB9DC8C33C2B025A91737077A7E9CC5E5")
            ), String.class);
        
        // token - delete
        assertSuccess(
            mvc.perform(MockMvcRequestBuilders
                .delete("/api/token")
                .header(Constants.AUTHORIZATION, tmpToken)
            ));
    }
    public void nodeTest() throws Exception {

        // node - post
        textNode = assertResult(mvc.perform(MockMvcRequestBuilders.post("/api/node").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(newNode(Text.TYPE_NAME, "Text Node", text)))
        ), Node.class);
        imageNode = assertResult(mvc.perform(MockMvcRequestBuilders.post("/api/node").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(newNode(Image.TYPE_NAME, "Image Node", image)))
        ), Node.class);
        musicNode = assertResult(mvc.perform(MockMvcRequestBuilders.post("/api/node").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(newNode(Music.TYPE_NAME, "Music Node", music)))
        ), Node.class);
        videoNode = assertResult(mvc.perform(MockMvcRequestBuilders.post("/api/node").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(newNode(Video.TYPE_NAME, "Video Node", video)))
        ), Node.class);

        // node - get
        assertEquals(textNode.getMainData().getTitle(),
            assertResult(mvc.perform(MockMvcRequestBuilders
                .get("/api/node/" + textNode.getMainData().getId())
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            ), Node.class).getMainData().getTitle()
        );
        assertEquals(imageNode.getMainData().getTitle(),
            assertResult(mvc.perform(MockMvcRequestBuilders
                .get("/api/node/" + imageNode.getMainData().getId())
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            ), Node.class).getMainData().getTitle()
        );
        assertEquals(musicNode.getMainData().getTitle(),
            assertResult(mvc.perform(MockMvcRequestBuilders
                .get("/api/node/" + musicNode.getMainData().getId())
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            ), Node.class).getMainData().getTitle()
        );
        assertEquals(videoNode.getMainData().getTitle(),
            assertResult(mvc.perform(MockMvcRequestBuilders
                .get("/api/node/" + videoNode.getMainData().getId())
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
            ), Node.class).getMainData().getTitle()
        );

        // node - update
        textNode.getExtraData(Text.class).setContent("New Text Content");
        assertEquals(textNode.getExtraData(Text.class).getContent(),
            assertResult(mvc.perform(MockMvcRequestBuilders
                .put("/api/node/")
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(textNode))
            ), Node.class).getExtraData(Text.class).getContent()
        );
        imageNode.getExtraData(Image.class).setDescription("New Image Description");
        assertEquals(imageNode.getExtraData(Image.class).getDescription(),
            assertResult(mvc.perform(MockMvcRequestBuilders
                .put("/api/node/")
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(imageNode))
            ), Node.class).getExtraData(Image.class).getDescription()
        );
        musicNode.getExtraData(Music.class).setFormat("newMusicFormat");
        assertEquals(musicNode.getExtraData(Music.class).getFormat(),
            assertResult(mvc.perform(MockMvcRequestBuilders
                .put("/api/node/")
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(musicNode))
            ), Node.class).getExtraData(Music.class).getFormat()
        );
        videoNode.getExtraData(Video.class).setFormat("newVideoFormat");
        assertEquals(videoNode.getExtraData(Video.class).getFormat(),
            assertResult(mvc.perform(MockMvcRequestBuilders
                .put("/api/node/")
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(videoNode))
            ), Node.class).getExtraData(Video.class).getFormat()
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
        listNode = assertResult(mvc.perform(MockMvcRequestBuilders.post("/api/node").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(newListNode)))
            , Node.class);
        showList("listNode", listNode.getExtraList());
        assertEquals(4, listNode.getExtraList().size());

        // put
        listNode.getExtraList().remove(3);
        assertEquals(3,
            assertResult(mvc.perform(MockMvcRequestBuilders
                .put("/api/node/")
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(listNode))
            ), Node.class).getExtraList().size()
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
        tagNode = assertResult(mvc.perform(MockMvcRequestBuilders.post("/api/node").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(newTagNode)))
            , Node.class);
        showList("tagNode", tagNode.getExtraList());
        assertEquals(4, tagNode.getExtraList().size());

        // put
        tagNode.getExtraList().remove(3);
        assertEquals(3,
            assertResult(mvc.perform(MockMvcRequestBuilders
                .put("/api/node/")
                .header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(tagNode))
            ), Node.class).getExtraList().size()
        );
    }
    public void searchTest() throws Exception {

        // search - getAll
        assertNodes("getAll", 6, assertList(
            mvc.perform(MockMvcRequestBuilders
                .get("/api/search").header(Constants.AUTHORIZATION, token)
            )));

        // search - query
        assertNodes("'Text Node'", 1, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Query()
                    .addCondition("node_title", "=", "'Text Node'")
                ))
            )));
        assertNodes("'%Text%'", 1, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Query()
                    .addCondition("node_title", "like", "'%Text%'")
                ))
            )));
        assertNodes("'%Text%', ctime asc", 1, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Query()
                    .addCondition("node_title", "like", "'%Text%'")
                    .addSort("node_ctime", Sort.Direction.ASC)
                ))
            )));

        assertNodes("permission=PRIVATE", 6, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Query()
                    .setPermission(Permission.PRIVATE)
                ))
            )));
        assertNodes("permission=PUBLIC", 0, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Query()
                    .setPermission(Permission.PUBLIC)
                ))
            )));

        assertNodes("orTag='Test Tag'", 3, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Query()
                    .addOrTag(new Tag("Test Tag"))
                ))
            )));
        assertNodes("andTag='%Tag%'", 3, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Query()
                    .addAndTag(new Tag("Tag", false))
                ))
            )));
        assertNodes("notTag='%Node%'", 3, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Query()
                    .addNotTag(new Tag("Tag", false))
                ))
            )));
        assertNodes("orTag='%Tag%', andTag='%Test%'", 3, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Query()
                    .addOrTag(new Tag("Tag", false))
                    .addAndTag(new Tag("Test", false))
                ))
            )));
        assertNodes("orTag='%Tag%', andTag='%Test%', notTag='Test Tag'", 0, assertList(
            mvc.perform(MockMvcRequestBuilders
                .post("/api/search").header(Constants.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Query()
                    .addOrTag(new Tag("Tag", false))
                    .addAndTag(new Tag("Test", false))
                    .addNotTag(new Tag("Test Tag"))
                ))
            )));
    }

    private Node newNode(String type, String title, ExtraData extraData) {
        Node node = new NodeDTO(com.my.list.domain.Node.Companion.defaultNode());
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
            switch (item.itemStatus) {
                case EXIST:  status = "EXIST "; break;
                case NEW:    status = "NEW   "; break;
                case UPDATE: status = "UPDATE"; break;
                default: status = null; break;
            }
            System.out.println("[" + i.getAndIncrement() + "] " + status + " " + item.node.getMainData());
        });
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
    private void assertSuccess(ResultActions resultActions) throws Exception {
        assertResult(resultActions, Object.class);
    }
    private List<Node> assertList(ResultActions resultActions) throws Exception {
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<?, ?>> list = (List<LinkedHashMap<?, ?>>) assertResult(resultActions, List.class);
        return list.stream().map(map -> objectMapper.convertValue(map, Node.class)).collect(Collectors.toList());
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
