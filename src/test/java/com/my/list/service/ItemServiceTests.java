package com.my.list.service;

import com.my.list.data.Item;
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
public class ItemServiceTests {

    @Autowired
    private ItemService itemService;
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

        Item item1 = new Item();
        item1.setTitle("Post1");
        item1.setInfo("Content1");
        Item item2 = new Item();
        item2.setTitle("Post2");
        item2.setInfo("Content2");

        //Add
        itemService.addItem(user1, item1);
        itemService.addItem(user1, item2);

        //Get
        assertEquals(itemService.getItem(user1, item1.getId()).getTitle(), item1.getTitle());
        assertEquals(itemService.getItem(user1, item2.getId()).getTitle(), item2.getTitle());

        //Search
        itemService.search(user1, "Item").forEach(System.out::println);

        //Update
        item1.setTitle("NewTitle1");
        item2.setTitle("NewTitle2");
        itemService.updateItem(user1, item1.getId(), item1);
        itemService.updateItem(user1, item2.getId(), item2);

        //Get
        assertEquals(itemService.getItem(user1, item1.getId()).getTitle(), "NewTitle1");
        assertEquals(itemService.getItem(user1, item2.getId()).getTitle(), "NewTitle2");

        //Search
        itemService.search(user1, "New").forEach(System.out::println);

        //Add Tag
        tagService.addTag(user1, tag1);
        itemService.addTagToItem(user1, item2.getId(), tag1.getId());

        //Remove Tag
        itemService.removeTagFromItem(user1, item2.getId(), tag1.getId());

        //Get Tag
        assertEquals(tagService.getTag(user1, tag1.getId()).getTitle(), tag1.getTitle());

        //Remove
        itemService.removeItem(user1, item1.getId());
        itemService.removeItem(user1, item2.getId());

        //Get
        try {
            itemService.getItem(user1, item1.getId());
            fail();
        } catch (DataException ignored) {}
        try {
            itemService.getItem(user1, item2.getId());
            fail();
        } catch (DataException ignored) {}
    }
}
