package com.my.list.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostRepositoryTests {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void test() {
        User user = new User();
        user.setId(1);

        Tag tag1 = new Tag();
        tag1.setTitle("Tag1");
        tag1.setInfo("Info1");

        Post post1 = new Post();
        post1.setUserId(user.getId());
        post1.setTitle("Title1");
        post1.setContent("Content1");
        post1.getTags().add(tag1);

        postRepository.save(post1);

        postRepository.findAllByUserIdAndTagId(user.getId(), tag1.getId()).forEach(post -> {
            System.out.println(post.getTitle());
        });
        postRepository.findAllByUserIdAndTagTitle(user.getId(), tag1.getTitle()).forEach(post -> {
            System.out.println(post.getTitle());
        });
    }
}
