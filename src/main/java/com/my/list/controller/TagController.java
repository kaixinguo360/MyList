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
import java.util.List;
import java.util.Map;

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

    //Add
    @JSON
    @RequestMapping(method = RequestMethod.POST)
    public Tag addTag(@CurrentUser User user,
                      @RequestBody Tag tag) throws DataException {
        tag.setUpdatedTime(new Date());
        return tagService.add(user, tag);
    }

    //Update
    @JSON
    @RequestMapping(method = RequestMethod.PUT)
    public Tag updateTag(@CurrentUser User user,
                         @RequestBody Tag tag) throws DataException {
        tag.setUpdatedTime(new Date());
        return tagService.update(user, tag);
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

    //GetAllItems
    @JSON(type = Tag.class, exclude = "createdTime,updatedTime")
    @JSON(type = Item.class, exclude = "tags,texts,images,musics,videos,links")
    @RequestMapping(value = "/{tagId}/item", method = RequestMethod.GET)
    public Iterable<Item> getItemsByTagId(@CurrentUser User user,
                                          @PathVariable int tagId) {
        return itemService.getAllByTagId(user, tagId);
    }

    //AddTagsToItems
    @JSON
    @RequestMapping(value = "/item", method = RequestMethod.POST)
    public MessageResponse addTagsByItemIds(@CurrentUser User user,
                                            @RequestBody Map<String, List<Integer>> params,
                                            @RequestParam(required = false) boolean isClear) {
        List<Integer> itemIds = params.get("itemIds");
        List<Integer> tagIds = params.get("tagIds");
        itemService.addTags(user, itemIds, tagIds, isClear);
        return new MessageResponse("Add Tags Successful");
    }
}
