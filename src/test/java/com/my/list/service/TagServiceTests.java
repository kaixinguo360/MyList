package com.my.list.service;

import com.my.list.data.Tag;
import com.my.list.data.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TagServiceTests {

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
        Tag tag2 = new Tag();
        tag2.setTitle("Tag2");
        tag2.setInfo("Info2");

        //Add
        tagService.add(user1, tag1);
        tagService.add(user1, tag2);

        //Get
        assertEquals(tagService.get(user1, tag1.getId()).getTitle(), tag1.getTitle());
        assertEquals(tagService.get(user1, tag2.getId()).getTitle(), tag2.getTitle());

        //Search
        tagService.search(user1, "Tag").forEach(System.out::println);

        //Update
        tag1.setTitle("NewTitle1");
        tag2.setTitle("NewTitle2");
        tagService.update(user1, tag1);
        tagService.update(user1, tag2);

        //Get
        assertEquals(tagService.get(user1, tag1.getId()).getTitle(), tag1.getTitle());
        assertEquals(tagService.get(user1, tag2.getId()).getTitle(), tag2.getTitle());

        //Search
        tagService.search(user1, "New").forEach(System.out::println);

        //Remove
        tagService.remove(user1, tag1.getId());
        tagService.remove(user1, tag2.getId());

        //Get
        try {
            tagService.get(user1, tag1.getId());
            fail();
        } catch (DataException ignored) {}
        try {
            tagService.get(user1, tag2.getId());
            fail();
        } catch (DataException ignored) {}
    }
}
