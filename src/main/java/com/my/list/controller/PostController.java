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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final TagService tagService;
    private final PostService postService;

    private ResponseEntity successResponse = new ResponseEntity<>("Success!", HttpStatus.OK);
    private ResponseEntity errorResponse = new ResponseEntity<>("Error!", HttpStatus.INTERNAL_SERVER_ERROR);
    private ResponseEntity notFoundResponse = new ResponseEntity<>("Not Found!", HttpStatus.NOT_FOUND);

    @Autowired
    public PostController(TagService tagService,
                          PostService postService) {
        this.tagService = tagService;
        this.postService = postService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Object addPost(@CurrentUser User user,
                          String title,
                          @RequestParam(required = false) String content) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content != null ? content : "");
        if (postService.addPost(user, post)) {
            return successResponse;
        } else {
            logger.info("addPost: An Error Occur!");
            return errorResponse;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{postId}", method = RequestMethod.DELETE)
    public Object removePost(@CurrentUser User user,
                             @PathVariable int postId) {
        if (postService.removePost(user, postId)) {
            return successResponse;
        } else {
            logger.info("removePost: An Error Occur!");
            return errorResponse;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{postId}", method = RequestMethod.PUT)
    public Object updatePost(@CurrentUser User user,
                             @PathVariable int postId,
                             String title,
                             @RequestParam(required = false) String content) {
        Post post = postService.getPost(user, postId);
        if (post != null) {
            post.setTitle(title);
            post.setContent(content != null ? content : "");
            return successResponse;
        } else {
            logger.info("updatePost: Post Not Found!");
            return notFoundResponse;
        }
    }

    @JSON(type = Post.class, include = "id,title,content,createdTime,updatedTime")
    @RequestMapping(value = "/{postId}", method = RequestMethod.GET)
    public Object getPost(@CurrentUser User user,
                          @PathVariable int postId) {
        Post post = postService.getPost(user, postId);
        if (post != null) {
            return post;
        } else {
            logger.info("getPost: Post Not Found!");
            return notFoundResponse;
        }
    }

    @JSON(type = Post.class, include = "id,title,content,createdTime,updatedTime")
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Post> getPosts(@CurrentUser User user) {
        return postService.getAllPosts(user);
    }

    @JSON(type = Post.class, include = "id,title,content,createdTime,updatedTime")
    @RequestMapping(value = "/{postId}/tags", method = RequestMethod.GET)
    public Iterable<Tag> getTagsByPostId(@CurrentUser User user,
                                          @PathVariable int postId) {
        return tagService.getTagByPostId(user, postId);
    }

    @JSON(type = Post.class, include = "id,title,content,createdTime,updatedTime")
    @RequestMapping(value = "/title/{postTitle}/tags", method = RequestMethod.GET)
    public Iterable<Tag> getTagsByPostTitle(@CurrentUser User user,
                                             @PathVariable String postTitle) {
        return tagService.getTagByPostTitle(user, postTitle);
    }
}
