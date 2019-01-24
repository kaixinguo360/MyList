package com.my.list.service;

import com.my.list.data.Post;
import com.my.list.data.Tag;
import com.my.list.data.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostServiceTests {

    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;

    @Test
    public void test() throws DataException {
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
        postService.addPost(user1, post1);
        postService.addPost(user1, post2);

        //Get
        assertEquals(postService.getPost(user1, post1.getId()).getTitle(), post1.getTitle());
        assertEquals(postService.getPost(user1, post2.getId()).getTitle(), post2.getTitle());

        //Search
        postService.search(user1, "Post").forEach(System.out::println);

        //Update
        post1.setTitle("NewTitle1");
        post2.setTitle("NewTitle2");
        postService.updatePost(user1, post1.getId(), post1);
        postService.updatePost(user1, post2.getId(), post2);

        //Get
        assertEquals(postService.getPost(user1, post1.getId()).getTitle(), "NewTitle1");
        assertEquals(postService.getPost(user1, post2.getId()).getTitle(), "NewTitle2");

        //Search
        postService.search(user1, "New").forEach(System.out::println);

        //Add Tag
        tagService.addTag(user1, tag1);
        postService.addTagToPost(user1, post2.getId(), tag1.getId());

        //Remove Tag
        postService.removeTagFromPost(user1, post2.getId(), tag1.getId());

        //Get Tag
        assertEquals(tagService.getTag(user1, tag1.getId()).getTitle(), tag1.getTitle());

        //Remove
        postService.removePost(user1, post1.getId());
        postService.removePost(user1, post2.getId());

        //Get
        try {
            postService.getPost(user1, post1.getId());
            fail();
        } catch (DataException ignored) {}
        try {
            postService.getPost(user1, post2.getId());
            fail();
        } catch (DataException ignored) {}
    }
}
