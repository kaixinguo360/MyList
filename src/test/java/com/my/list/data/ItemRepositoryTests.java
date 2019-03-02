package com.my.list.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ItemRepositoryTests {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void test() {
        User user = new User();
        user.setId(1);

        Tag tag1 = new Tag();
        tag1.setTitle("Tag1");
        tag1.setInfo("Info1");

        Item item1 = new Item();
        item1.setUserId(user.getId());
        item1.setTitle("Title1");
        item1.setInfo("Content1");
        item1.getTags().add(tag1);

        itemRepository.save(item1);

        itemRepository.findAllByUserIdAndTagId(user.getId(), tag1.getId()).forEach(post -> {
            System.out.println(post.getTitle());
        });
        itemRepository.findAllByUserIdAndTagTitle(user.getId(), tag1.getTitle()).forEach(post -> {
            System.out.println(post.getTitle());
        });
    }
}
