package com.my.list.controller;

import com.my.list.data.Item;
import com.my.list.data.MyList;
import com.my.list.data.User;
import com.my.list.json.JSON;
import com.my.list.service.DataException;
import com.my.list.service.ItemService;
import com.my.list.service.MyListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@Authorization
@RequestMapping("/api/list")
public class MyListController {

    private final MyListService myListService;
    private final ItemService itemService;

    @Autowired
    public MyListController(MyListService myListService,
                            ItemService itemService) {
        this.myListService = myListService;
        this.itemService = itemService;
    }


    // ------------------------------ List ------------------------------ //

    //Add
    @JSON
    @RequestMapping(method = RequestMethod.POST)
    public MyList addList(@CurrentUser User user,
                          @RequestBody MyList list) throws DataException {
        list.setUpdatedTime(new Date());
        return myListService.add(user, list);
    }

    //Update
    @JSON
    @RequestMapping(method = RequestMethod.PUT)
    public MyList updateList(@CurrentUser User user,
                             @RequestBody MyList list) throws DataException {
        list.setUpdatedTime(new Date());
        return myListService.update(user, list);
    }

    //Remove
    @JSON
    @RequestMapping(method = RequestMethod.DELETE)
    public MessageResponse removeMyList(@CurrentUser User user,
                                     @RequestBody MyList list) throws DataException {
        myListService.remove(user, list.getId());
        return new MessageResponse("Remove List Successful");
    }

    //Get
    @JSON
    @RequestMapping(value = "/{listId}", method = RequestMethod.GET)
    public MyList getList(@CurrentUser User user,
                      @PathVariable int listId) throws DataException {
        return myListService.get(user, listId);
    }

    //GetAll
    @JSON
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<MyList> getLists(@CurrentUser User user,
                                 @RequestParam(required = false) String search) {
        if (StringUtils.isEmpty(search)) {
            return myListService.getAll(user);
        } else {
            return myListService.search(user, search);
        }
    }


    // ------------------------------ Item ------------------------------ //

    //GetAll
    @JSON(type = MyList.class, exclude = "createdTime,updatedTime")
    @JSON(type = Item.class, exclude = "tags,texts,images,musics,videos,links")
    @RequestMapping(value = "/{listId}/item", method = RequestMethod.GET)
    public Iterable<Item> getItemsByListId(@CurrentUser User user,
                                          @PathVariable int listId) {
        return itemService.getAllByListId(user, listId);
    }
}
