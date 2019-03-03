package com.my.list.service;

import com.my.list.data.Item;
import com.my.list.data.MyList;
import com.my.list.data.Tag;
import com.my.list.data.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceTests {

    @Autowired
    private ItemService itemService;
    @Autowired
    private TagService tagService;
    @Autowired
    private MyListService myListService;

    @Test
    public void test() throws DataException {
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);

        Tag tag1 = new Tag();
        tag1.setTitle("Tag1");
        tag1.setInfo("Info1");

        MyList list1 = new MyList();
        list1.setTitle("Tag1");
        list1.setInfo("Info1");

        Item item1 = new Item();
        item1.setTitle("Post1");
        item1.setInfo("Content1");
        Item item2 = new Item();
        item2.setTitle("Post2");
        item2.setInfo("Content2");

        //Add
        itemService.save(user1, item1);
        itemService.save(user1, item2);

        //Get
        assertEquals(itemService.get(user1, item1.getId()).getTitle(), item1.getTitle());
        assertEquals(itemService.get(user1, item2.getId()).getTitle(), item2.getTitle());

        //Search
        itemService.search(user1, "Item").forEach(System.out::println);

        //Update
        item1.setTitle("NewTitle1");
        item2.setTitle("NewTitle2");
        itemService.save(user1, item1);
        itemService.save(user1, item2);

        //Get
        assertEquals(itemService.get(user1, item1.getId()).getTitle(), "NewTitle1");
        assertEquals(itemService.get(user1, item2.getId()).getTitle(), "NewTitle2");

        //Search
        itemService.search(user1, "New").forEach(System.out::println);

        //Add Tag
        tagService.save(user1, tag1);

        //Get Tag
        assertEquals(tagService.get(user1, tag1.getId()).getTitle(), tag1.getTitle());

        //Get Tags From Item
        itemService.getAllByTagId(user1, tag1.getId()).forEach(item -> {
            if (item.getId() == item2.getId()) {
                fail();
            }
        });

        //Add Tag To Item
        item2.getTags().add(tag1);
        itemService.save(user1, item2);

        //Get Tags From Item
        AtomicBoolean flag = new AtomicBoolean(false);
        itemService.getAllByTagId(user1, tag1.getId()).forEach(item -> {
            if (item.getId() == item2.getId()) {
                flag.set(true);
            }
        });
        assertTrue(flag.get());

        //Add List
        myListService.addList(user1, list1);
        itemService.setList(user1, item2.getId(), list1.getId());
        assertEquals(itemService.get(user1, item2.getId()).getList().getTitle(), list1.getTitle());

        //Remove List
        itemService.removeList(user1, item2.getId());
        assertNull(itemService.get(user1, item2.getId()).getList());

        //Get List
        assertEquals(myListService.getList(user1, list1.getId()).getTitle(), list1.getTitle());

        //Remove
        itemService.remove(user1, item1.getId());
        itemService.remove(user1, item2.getId());

        //Get
        try {
            itemService.get(user1, item1.getId());
            fail();
        } catch (DataException ignored) {}
        try {
            itemService.get(user1, item2.getId());
            fail();
        } catch (DataException ignored) {}
    }
}
