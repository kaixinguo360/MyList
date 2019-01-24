package com.my.list.controller;

import com.my.list.data.Post;
import com.my.list.data.Tag;
import com.my.list.data.User;
import com.my.list.json.JSON;
import com.my.list.service.DataException;
import com.my.list.service.PostService;
import com.my.list.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Authorization
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final PostService postService;

    @Autowired
    public TagController(TagService tagService,
                         PostService postService) {
        this.tagService = tagService;
        this.postService = postService;
    }


    // ------------------------------ Tags ------------------------------ //

    //Add
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public MessageResponse addTag(@CurrentUser User user,
                                  @RequestParam String title,
                                  @RequestParam(required = false) String info) throws DataException {
        Tag tag = new Tag();
        tag.setTitle(title);
        tag.setInfo(info != null ? info : "");
        tagService.addTag(user, tag);
        return new MessageResponse("Add Tag Successful");
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
    @JSON(type = Tag.class, include = "id,title,info,createdTime,updatedTime")
    @RequestMapping(value = "/{tagId}", method = RequestMethod.GET)
    public Tag getTag(@CurrentUser User user,
                      @PathVariable int tagId) throws DataException {
        return tagService.getTag(user, tagId);
    }

    //GetAll
    @JSON(type = Tag.class, include = "id,title,info")
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Tag> getTags(@CurrentUser User user) {
        return tagService.getAllTags(user);
    }


    // ------------------------------ Posts ------------------------------ //

    //GetAll
    @JSON(type = Post.class, include = "id,title,content,createdTime,updatedTime")
    @RequestMapping(value = "/{tagId}/posts", method = RequestMethod.GET)
    public Iterable<Post> getPostsByTagId(@CurrentUser User user,
                                          @PathVariable int tagId) {
        return postService.getPostsByTagId(user, tagId);
    }
}
