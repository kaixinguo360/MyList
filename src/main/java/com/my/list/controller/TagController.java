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


    // ------------------------------ Tags ------------------------------ //

    //Add
    @JSON(type = Tag.class, include = "id,createdTime,updatedTime,title,info")
    @RequestMapping(method = RequestMethod.POST)
    public Tag addTag(@CurrentUser User user,
                                  @RequestParam String title,
                                  @RequestParam(required = false) String info) throws DataException {
        Tag tag = new Tag();
        tag.setTitle(title);
        tag.setInfo(info != null ? info : "");
        tagService.addTag(user, tag);
        return tag;
    }

    //Remove
    @ResponseBody
    @RequestMapping(value = "/{tagId}", method = RequestMethod.DELETE)
    public MessageResponse removeTag(@CurrentUser User user,
                                     @PathVariable int tagId) throws DataException {
        tagService.removeTag(user, tagId);
        return new MessageResponse("Remove Tag Successful");
    }

    //Update
    @ResponseBody
    @RequestMapping(value = "/{tagId}", method = RequestMethod.PUT)
    public MessageResponse updateTag(@CurrentUser User user,
                                     @PathVariable int tagId,
                                     @RequestParam String title,
                                     @RequestParam(required = false) String info) throws DataException {
        Tag tag = tagService.getTag(user, tagId);
        tag.setTitle(title);
        tag.setInfo(info != null ? info : "");
        tagService.updateTag(user, tagId, tag);
        return new MessageResponse("Update Tag Successful");
    }

    //Get
    @JSON(type = Tag.class, include = "id,createdTime,updatedTime,title,info")
    @RequestMapping(value = "/{tagId}", method = RequestMethod.GET)
    public Tag getTag(@CurrentUser User user,
                      @PathVariable int tagId) throws DataException {
        return tagService.getTag(user, tagId);
    }

    //GetAll
    @JSON(type = Tag.class, include = "id,createdTime,updatedTime,title,info")
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Tag> getTags(@CurrentUser User user) {
        return tagService.getAllTags(user);
    }

    //Search
    @JSON(type = Tag.class, include = "id,createdTime,updatedTime,title,info")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Iterable<Tag> searchTags(@CurrentUser User user,
                                    @RequestParam String title) {
        return tagService.search(user, title);
    }


    // ------------------------------ Posts ------------------------------ //

    //GetAll
    @JSON(type = Item.class, include = "id,createdTime,updatedTime,title,info,url,img")
    @RequestMapping(value = "/{tagId}/item", method = RequestMethod.GET)
    public Iterable<Item> getPostsByTagId(@CurrentUser User user,
                                          @PathVariable int tagId) {
        return itemService.getItemsByTagId(user, tagId);
    }
}
