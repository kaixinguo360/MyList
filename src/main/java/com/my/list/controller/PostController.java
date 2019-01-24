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
@RequestMapping("/posts")
public class PostController {

    private final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final TagService tagService;
    private final PostService postService;

    @Autowired
    public PostController(TagService tagService,
                          PostService postService) {
        this.tagService = tagService;
        this.postService = postService;
    }


    // ------------------------------ Posts ------------------------------ //

    //Add
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public MessageResponse addPost(@CurrentUser User user,
                                   @RequestParam String title,
                                   @RequestParam(required = false) String content) throws RequestException {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content != null ? content : "");
        if (postService.addPost(user, post)) {
            return new MessageResponse("Add Post Successful");
        } else {
            logger.info("addPost: An Error Occurred");
            throw new RequestException("An Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Remove
    @ResponseBody
    @RequestMapping(value = "/{postId}", method = RequestMethod.DELETE)
    public MessageResponse removePost(@CurrentUser User user,
                                      @PathVariable int postId) throws RequestException {
        if (postService.removePost(user, postId)) {
            return new MessageResponse("Remove Post Successful");
        } else {
            logger.info("removePost: An Error Occurred");
            throw new RequestException("An Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update
    @ResponseBody
    @RequestMapping(value = "/{postId}", method = RequestMethod.PUT)
    public MessageResponse updatePost(@CurrentUser User user,
                                      @PathVariable int postId,
                                      @RequestParam String title,
                                      @RequestParam(required = false) String content) throws RequestException {
        Post post = postService.getPost(user, postId);
        if (post != null) {
            post.setTitle(title);
            post.setContent(content != null ? content : "");
            if (postService.updatePost(user, postId, post)) {
                return new MessageResponse("Update Post Successful");
            } else {
                logger.info("updatePost: An Error Occurred");
                throw new RequestException("An Error Occurred", HttpStatus.NOT_FOUND);
            }
        } else {
            logger.info("updatePost: Post Not Found");
            throw new RequestException("Post Not Found", HttpStatus.NOT_FOUND);
        }
    }

    //Get
    @JSON(type = Post.class, include = "id,title,content,tags,createdTime,updatedTime")
    @JSON(type = Tag.class, include = "id,title,info")
    @RequestMapping(value = "/{postId}", method = RequestMethod.GET)
    public Post getPost(@CurrentUser User user,
                        @PathVariable int postId) throws RequestException {
        Post post = postService.getPost(user, postId);
        if (post != null) {
            return post;
        } else {
            logger.info("getPost: Post Not Found");
            throw new RequestException("Post Not Found", HttpStatus.NOT_FOUND);
        }
    }

    //GetAll
    @JSON(type = Post.class, include = "id,title,content,createdTime,updatedTime")
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Post> getPosts(@CurrentUser User user) {
        return postService.getAllPosts(user);
    }


    // ------------------------------ Tags ------------------------------ //

    //Add
    @ResponseBody
    @RequestMapping(value = "/{postId}/tags/{tagId}", method = RequestMethod.POST)
    public MessageResponse addTagToPost(@CurrentUser User user,
                                        @PathVariable int postId,
                                        @PathVariable int tagId) throws RequestException {
        if (postService.addTagToPost(user, postId, tagId)) {
            return new MessageResponse("Add Tag To Post Successful");
        } else {
            logger.info("addTagToPost: An Error Occurred");
            throw new RequestException("An Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Remove
    @ResponseBody
    @RequestMapping(value = "/{postId}/tags/{tagId}", method = RequestMethod.DELETE)
    public MessageResponse removeTagFromPost(@CurrentUser User user, 
                                             @PathVariable int postId, 
                                             @PathVariable int tagId) throws RequestException {
        if (postService.removeTagFromPost(user, postId, tagId)) {
            return new MessageResponse("Remove Tag From Post Successful");
        } else {
            logger.info("removeTagFromPost: An Error Occurred");
            throw new RequestException("An Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GetAll
    @JSON(type = Tag.class, include = "id,title,info,createdTime,updatedTime")
    @RequestMapping(value = "/{postId}/tags", method = RequestMethod.GET)
    public Iterable<Tag> getTagsFromPost(@CurrentUser User user,
                                         @PathVariable int postId) {
        return tagService.getTagByPostId(user, postId);
    }
}
