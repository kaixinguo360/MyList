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
import org.springframework.web.bind.annotation.*;

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


    // ------------------------------ Lists ------------------------------ //

    //Add
    @JSON(type = MyList.class, include = "id,createdTime,updatedTime,title,info")
    @RequestMapping(method = RequestMethod.POST)
    public MyList addList(@CurrentUser User user,
                                  @RequestParam String title,
                                  @RequestParam(required = false) String info) throws DataException {
        MyList list = new MyList();
        list.setTitle(title);
        list.setInfo(info != null ? info : "");
        myListService.addList(user, list);
        return list;
    }

    //Remove
    @ResponseBody
    @RequestMapping(value = "/{listId}", method = RequestMethod.DELETE)
    public MessageResponse removeList(@CurrentUser User user,
                                     @PathVariable int listId) throws DataException {
        myListService.removeList(user, listId);
        return new MessageResponse("Remove List Successful");
    }

    //Update
    @ResponseBody
    @RequestMapping(value = "/{listId}", method = RequestMethod.PUT)
    public MessageResponse updateList(@CurrentUser User user,
                                     @PathVariable int listId,
                                     @RequestParam String title,
                                     @RequestParam(required = false) String info) throws DataException {
        MyList list = myListService.getList(user, listId);
        list.setTitle(title);
        list.setInfo(info != null ? info : "");
        myListService.updateList(user, listId, list);
        return new MessageResponse("Update List Successful");
    }

    //Get
    @JSON(type = MyList.class, include = "id,createdTime,updatedTime,title,info")
    @RequestMapping(value = "/{listId}", method = RequestMethod.GET)
    public MyList getList(@CurrentUser User user,
                      @PathVariable int listId) throws DataException {
        return myListService.getList(user, listId);
    }

    //GetAll
    @JSON(type = MyList.class, include = "id,createdTime,updatedTime,title,info")
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<MyList> getLists(@CurrentUser User user) {
        return myListService.getAllLists(user);
    }

    //Search
    @JSON(type = MyList.class, include = "id,createdTime,updatedTime,title,info")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Iterable<MyList> searchLists(@CurrentUser User user,
                                    @RequestParam String title) {
        return myListService.search(user, title);
    }


    // ------------------------------ Items ------------------------------ //

    //GetAll
    @JSON(type = Item.class, include = "id,createdTime,updatedTime,title,info,url,img")
    @RequestMapping(value = "/{listId}/item", method = RequestMethod.GET)
    public Iterable<Item> getItemsByListId(@CurrentUser User user,
                                          @PathVariable int listId) {
        return itemService.getAllByListId(user, listId);
    }

    //Add
    @ResponseBody
    @RequestMapping(value = "/{listId}/item/{itemId}", method = RequestMethod.POST)
    public MessageResponse addTagToItem(@CurrentUser User user,
                                        @PathVariable int listId,
                                        @PathVariable int itemId) throws DataException {
        itemService.setList(user, itemId, listId);
        return new MessageResponse("Set List To Item Successful");
    }
}
