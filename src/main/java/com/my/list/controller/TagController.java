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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@Authorization
@RequestMapping("/api/tag")
public class TagController {

    private final TagService tagService;
    private final ItemService itemService;

    @Autowired
    public TagController(TagService tagService,
                         ItemService itemService) {
        this.tagService = tagService;
        this.itemService = itemService;
    }


    // ------------------------------ Tag ------------------------------ //

    //Save
    @JSON
    @RequestMapping(method = {
        RequestMethod.POST,
        RequestMethod.PUT
    })
    public Tag saveTag(@CurrentUser User user,
                       @RequestBody Tag tag) throws DataException {
        tag.setUpdatedTime(new Date());

        return tagService.save(user, tag);
    }

    //Remove
    @JSON
    @RequestMapping(method = RequestMethod.DELETE)
    public MessageResponse removeTag(@CurrentUser User user,
                                     @RequestBody Tag tag) throws DataException {
        tagService.remove(user, tag.getId());
        return new MessageResponse("Remove Tag Successful");
    }

    //Get
    @JSON
    @RequestMapping(value = "/{tagId}", method = RequestMethod.GET)
    public Tag getTag(@CurrentUser User user,
                      @PathVariable int tagId) throws DataException {
        return tagService.get(user, tagId);
    }

    //GetAll
    @JSON
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Tag> getTags(@CurrentUser User user,
                                 @RequestParam(required = false) String search) {
        if (StringUtils.isEmpty(search)) {
            return tagService.getAll(user);
        } else {
            return tagService.search(user, search);
        }
    }


    // ------------------------------ Item ------------------------------ //

    //GetAll
    @JSON(type = Tag.class, exclude = "createdTime,updatedTime")
    @JSON(type = Item.class, exclude = "texts,images,musics,videos,links")
    @RequestMapping(value = "/{tagId}/item", method = RequestMethod.GET)
    public Iterable<Item> getItemsByTagId(@CurrentUser User user,
                                          @PathVariable int tagId) {
        return itemService.getAllByTagId(user, tagId);
    }
}
