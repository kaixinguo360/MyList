package com.my.list.controller;

import com.my.list.data.Item;
import com.my.list.data.Tag;
import com.my.list.data.User;
import com.my.list.json.JSON;
import com.my.list.service.DataException;
import com.my.list.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@Authorization
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    // ------------------------------ Item ------------------------------ //

    //Save
    @JSON(type = Tag.class, exclude = "createdTime,updatedTime")
    @RequestMapping(method = {
        RequestMethod.POST,
        RequestMethod.PUT
    })
    public Item saveItem(@CurrentUser User user,
                         @RequestBody Item item) throws DataException {
        item.setUpdatedTime(new Date());

        item.getTexts().forEach((c -> {
            c.setUpdatedTime(new Date());
        }));
        item.getImages().forEach((c -> {
            c.setUpdatedTime(new Date());
        }));
        item.getMusics().forEach((c -> {
            c.setUpdatedTime(new Date());
        }));
        item.getVideos().forEach((c -> {
            c.setUpdatedTime(new Date());
        }));
        item.getLinks().forEach((c -> {
            c.setUpdatedTime(new Date());
        }));

        return itemService.save(user, item);
    }

    //Remove
    @JSON
    @RequestMapping(method = RequestMethod.DELETE)
    public MessageResponse removeItem(@CurrentUser User user,
                                      @RequestBody Item item) throws DataException {
        itemService.remove(user, item.getId());
        return new MessageResponse("Remove Item Successful");
    }

    //Get
    @JSON(type = Tag.class, exclude = "createdTime,updatedTime")
    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public Item getItem(@CurrentUser User user,
                        @PathVariable int itemId) throws DataException {
        return itemService.get(user, itemId);
    }

    //GetAll
    @JSON(type = Tag.class, exclude = "createdTime,updatedTime")
    @JSON(type = Item.class, exclude = "texts,images,musics,videos,links")
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Item> getItems(@CurrentUser User user,
                                   @RequestParam(required = false) String search) {
        if (StringUtils.isEmpty(search)) {
            return itemService.getAll(user);
        } else {
            return itemService.search(user, search);
        }
    }
}
