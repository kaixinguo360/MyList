package com.my.list.service;

import com.my.list.data.Post;
import com.my.list.data.Tag;
import com.my.list.data.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostServiceTests {

    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;

    @Test
    public void test() {
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);

        Tag tag1 = new Tag();
        tag1.setTitle("Tag1");
        tag1.setInfo("Info1");

        Post post1 = new Post();
        post1.setTitle("Post1");
        post1.setContent("Content1");
        Post post2 = new Post();
        post2.setTitle("Post2");
        post2.setContent("Content2");

        //Add
        assertTrue(postService.addPost(user1, post1));
        assertTrue(postService.addPost(user1, post2));

        //Get
        assertEquals(postService.getPost(user1, post1.getId()).getTitle(), post1.getTitle());
        assertEquals(postService.getPost(user1, post2.getId()).getTitle(), post2.getTitle());

        //Search
        postService.search(user1, "Post").forEach(System.out::println);

        //Update
        post1.setTitle("NewTitle1");
        post2.setTitle("NewTitle2");
        assertTrue(postService.updatePost(user1, post1.getId(), post1));
        assertTrue(postService.updatePost(user1, post2.getId(), post2));

        //Get
        assertEquals(postService.getPost(user1, post1.getId()).getTitle(), "NewTitle1");
        assertEquals(postService.getPost(user1, post2.getId()).getTitle(), "NewTitle2");

        //Search
        postService.search(user1, "New").forEach(System.out::println);

        //Add Tag
        assertTrue(tagService.addTag(user1, tag1));
        assertTrue(postService.addTagToPost(user1, post2.getId(), tag1.getId()));

        //Remove Tag
        assertTrue(postService.removeTagFromPost(user1, post2.getId(), tag1.getId()));

        //Get Tag
        assertEquals(tagService.getTag(user1, tag1.getId()).getTitle(), tag1.getTitle());

        //Remove
        assertTrue(postService.removePost(user1, post1.getId()));
        assertTrue(postService.removePost(user1, post2.getId()));

        //Get
        assertNull(postService.getPost(user1, post1.getId()));
        assertNull(postService.getPost(user1, post2.getId()));
    }
}
