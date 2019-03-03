package com.my.list.service;

import com.my.list.data.MyList;
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
public class MyListServiceTests {

    @Autowired
    private MyListService myListService;

    @Test
    public void test() throws DataException {
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);

        MyList list1 = new MyList();
        list1.setTitle("List1");
        list1.setInfo("Info1");
        MyList list2 = new MyList();
        list2.setTitle("List2");
        list2.setInfo("Info2");

        //Add
        myListService.add(user1, list1);
        myListService.add(user1, list2);

        //Get
        assertEquals(myListService.get(user1, list1.getId()).getTitle(), list1.getTitle());
        assertEquals(myListService.get(user1, list2.getId()).getTitle(), list2.getTitle());

        //Search
        myListService.search(user1, "List").forEach(System.out::println);

        //Update
        list1.setTitle("NewTitle1");
        list2.setTitle("NewTitle2");
        myListService.update(user1, list1);
        myListService.update(user1, list2);

        //Get
        assertEquals(myListService.get(user1, list1.getId()).getTitle(), list1.getTitle());
        assertEquals(myListService.get(user1, list2.getId()).getTitle(), list2.getTitle());

        //Search
        myListService.search(user1, "New").forEach(System.out::println);

        //Remove
        myListService.remove(user1, list1.getId());
        myListService.remove(user1, list2.getId());

        //Get
        try {
            myListService.get(user1, list1.getId());
            fail();
        } catch (DataException ignored) {}
        try {
            myListService.get(user1, list2.getId());
            fail();
        } catch (DataException ignored) {}
    }
}
