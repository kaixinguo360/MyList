package com.my.list.service;

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
public class TagServiceTests {

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
        Tag tag2 = new Tag();
        tag2.setTitle("Tag2");
        tag2.setInfo("Info2");

        //Add
        assertTrue(tagService.addTag(user1, tag1));
        assertTrue(tagService.addTag(user1, tag2));

        //Get
        assertEquals(tagService.getTag(user1, tag1.getId()).getTitle(), tag1.getTitle());
        assertEquals(tagService.getTag(user1, tag2.getId()).getTitle(), tag2.getTitle());

        //Search
        tagService.search(user1, "Tag").forEach(System.out::println);

        //Update
        tag1.setTitle("NewTitle1");
        tag2.setTitle("NewTitle2");
        assertTrue(tagService.updateTag(user1, tag1.getId(), tag1));
        assertTrue(tagService.updateTag(user1, tag2.getId(), tag2));

        //Get
        assertEquals(tagService.getTag(user1, tag1.getId()).getTitle(), tag1.getTitle());
        assertEquals(tagService.getTag(user1, tag2.getId()).getTitle(), tag2.getTitle());

        //Search
        tagService.search(user1, "New").forEach(System.out::println);

        //Remove
        assertTrue(tagService.removeTag(user1, tag1.getId()));
        assertTrue(tagService.removeTag(user1, tag2.getId()));

        //Get
        assertNull(tagService.getTag(user1, tag1.getId()));
        assertNull(tagService.getTag(user1, tag2.getId()));
    }
}
