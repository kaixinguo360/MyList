package com.my.list.controller;

import com.my.list.data.Post;
import com.my.list.data.Tag;
import com.my.list.data.User;
import com.my.list.json.JSON;
import com.my.list.service.PostService;
import com.my.list.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Authorization
@RequestMapping("/tags")
public class TagController {

    private final Logger logger = LoggerFactory.getLogger(TagController.class);
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
                                  @RequestParam(required = false) String info) throws RequestException {
        Tag tag = new Tag();
        tag.setTitle(title);
        tag.setInfo(info != null ? info : "");
        if (tagService.addTag(user, tag)) {
            return new MessageResponse("Add Tag Successful");
        } else {
            logger.info("addTag: An Error Occurred");
            throw new RequestException("An Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Remove
    @ResponseBody
    @RequestMapping(value = "/{tagId}", method = RequestMethod.DELETE)
    public MessageResponse removeTag(@CurrentUser User user,
                                     @PathVariable int tagId) throws RequestException {
        if (tagService.removeTag(user, tagId)) {
            return new MessageResponse("Remove Tag Successful");
        } else {
            logger.info("removeTag: An Error Occurred");
            throw new RequestException("An Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update
    @ResponseBody
    @RequestMapping(value = "/{tagId}", method = RequestMethod.PUT)
    public MessageResponse updateTag(@CurrentUser User user,
                                     @PathVariable int tagId,
                                     @RequestParam String title,
                                     @RequestParam(required = false) String info) throws RequestException {
        Tag tag = tagService.getTag(user, tagId);
        if (tag != null) {
            tag.setTitle(title);
            tag.setInfo(info != null ? info : "");
            if (tagService.updateTag(user, tagId, tag)) {
                return new MessageResponse("Update Tag Successful");
            } else {
                logger.info("updateTag: An Error Occurred");
                throw new RequestException("An Error Occurred", HttpStatus.NOT_FOUND);
            }
        } else {
            logger.info("updateTag: Tag Not Found");
            throw new RequestException("Tag Not Found", HttpStatus.NOT_FOUND);
        }
    }

    //Get
    @JSON(type = Tag.class, include = "id,title,info,createdTime,updatedTime")
    @RequestMapping(value = "/{tagId}", method = RequestMethod.GET)
    public Tag getTag(@CurrentUser User user,
                      @PathVariable int tagId) throws RequestException {
        Tag tag = tagService.getTag(user, tagId);
        if (tag != null) {
            return tag;
        } else {
            logger.info("getTag: Tag Not Found");
            throw new RequestException("Tag Not Found", HttpStatus.NOT_FOUND);
        }
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
