package com.my.list.controller;

import com.my.list.data.Item;
import com.my.list.data.Tag;
import com.my.list.data.User;
import com.my.list.json.JSON;
import com.my.list.service.DataException;
import com.my.list.service.ItemService;
import com.my.list.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Authorization
@RequestMapping("/api/item")
public class ItemController {

    private final TagService tagService;
    private final ItemService itemService;

    @Autowired
    public ItemController(TagService tagService,
                          ItemService itemService) {
        this.tagService = tagService;
        this.itemService = itemService;
    }


    // ------------------------------ Items ------------------------------ //

    //Add
    @JSON(type = Item.class, include = "id,createdTime,updatedTime,title,info,url,img")
    @RequestMapping(method = RequestMethod.POST)
    public Item addItem(@CurrentUser User user,
                        @RequestParam String title,
                        @RequestParam(required = false) String content) throws DataException {
        Item item = new Item();
        item.setTitle(title);
        item.setInfo(content != null ? content : "");
        itemService.addItem(user, item);
        return item;
    }

    //Remove
    @ResponseBody
    @RequestMapping(value = "/{itemId}", method = RequestMethod.DELETE)
    public MessageResponse removeItem(@CurrentUser User user,
                                      @PathVariable int itemId) throws DataException {
        itemService.removeItem(user, itemId);
        return new MessageResponse("Remove Item Successful");
    }

    //Update
    @ResponseBody
    @RequestMapping(value = "/{itemId}", method = RequestMethod.PUT)
    public MessageResponse updateItem(@CurrentUser User user,
                                      @PathVariable int itemId,
                                      @RequestParam String title,
                                      @RequestParam(required = false) String content) throws DataException {
        Item item = itemService.getItem(user, itemId);
        item.setTitle(title);
        item.setInfo(content != null ? content : "");
        itemService.updateItem(user, itemId, item);
        return new MessageResponse("Update Item Successful");
    }

    //Get
    @JSON(type = Item.class, include = "id,createdTime,updatedTime,title,info,url,img,tags")
    @JSON(type = Tag.class, include = "id,title,info")
    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public Item getItem(@CurrentUser User user,
                        @PathVariable int itemId) throws DataException {
        return itemService.getItem(user, itemId);
    }

    //GetAll
    @JSON(type = Item.class, include = "id,createdTime,updatedTime,title,info,url,img")
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Item> getItems(@CurrentUser User user) {
        return itemService.getAllItems(user);
    }

    //Search
    @JSON(type = Item.class, include = "id,createdTime,updatedTime,title,info,url,img")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Iterable<Item> searchItems(@CurrentUser User user,
                                      @RequestParam String title) {
        return itemService.search(user, title);
    }


    // ------------------------------ Tags ------------------------------ //

    //Add
    @ResponseBody
    @RequestMapping(value = "/{itemId}/tag/{tagId}", method = RequestMethod.POST)
    public MessageResponse addTagToItem(@CurrentUser User user,
                                        @PathVariable int itemId,
                                        @PathVariable int tagId) throws DataException {
        itemService.addTagToItem(user, itemId, tagId);
        return new MessageResponse("Add Tag To Item Successful");
    }

    //Remove
    @ResponseBody
    @RequestMapping(value = "/{itemId}/tag/{tagId}", method = RequestMethod.DELETE)
    public MessageResponse removeTagFromItem(@CurrentUser User user,
                                             @PathVariable int itemId,
                                             @PathVariable int tagId) throws DataException {
        itemService.removeTagFromItem(user, itemId, tagId);
        return new MessageResponse("Remove Tag From Item Successful");
    }

    //GetAll
    @JSON(type = Tag.class, include = "id,createdTime,updatedTime,title,info")
    @RequestMapping(value = "/{itemId}/tag", method = RequestMethod.GET)
    public Iterable<Tag> getTagsFromItem(@CurrentUser User user,
                                         @PathVariable int itemId) {
        return tagService.getTagsByItemId(user, itemId);
    }
}
