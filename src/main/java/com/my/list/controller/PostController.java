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
@RequestMapping("/posts")
public class PostController {

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
    @JSON(type = Post.class, include = "id,title,content,createdTime,updatedTime")
    @RequestMapping(method = RequestMethod.POST)
    public Post addPost(@CurrentUser User user,
                                   @RequestParam String title,
                                   @RequestParam(required = false) String content) throws DataException {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content != null ? content : "");
        postService.addPost(user, post);
        return post;
    }

    //Remove
    @ResponseBody
    @RequestMapping(value = "/{postId}", method = RequestMethod.DELETE)
    public MessageResponse removePost(@CurrentUser User user,
                                      @PathVariable int postId) throws DataException {
        postService.removePost(user, postId);
        return new MessageResponse("Remove Post Successful");
    }

    //Update
    @ResponseBody
    @RequestMapping(value = "/{postId}", method = RequestMethod.PUT)
    public MessageResponse updatePost(@CurrentUser User user,
                                      @PathVariable int postId,
                                      @RequestParam String title,
                                      @RequestParam(required = false) String content) throws DataException {
        Post post = postService.getPost(user, postId);
        post.setTitle(title);
        post.setContent(content != null ? content : "");
        postService.updatePost(user, postId, post);
        return new MessageResponse("Update Post Successful");
    }

    //Get
    @JSON(type = Post.class, include = "id,title,content,tags,createdTime,updatedTime")
    @JSON(type = Tag.class, include = "id,title,info")
    @RequestMapping(value = "/{postId}", method = RequestMethod.GET)
    public Post getPost(@CurrentUser User user,
                        @PathVariable int postId) throws DataException {
        return postService.getPost(user, postId);
    }

    //GetAll
    @JSON(type = Post.class, include = "id,title,content,createdTime,updatedTime")
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Post> getPosts(@CurrentUser User user) {
        return postService.getAllPosts(user);
    }

    //Search
    @JSON(type = Post.class, include = "id,title,content,createdTime,updatedTime")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Iterable<Post> searchPosts(@CurrentUser User user,
                                      @RequestParam String title) {
        return postService.search(user, title);
    }


    // ------------------------------ Tags ------------------------------ //

    //Add
    @ResponseBody
    @RequestMapping(value = "/{postId}/tags/{tagId}", method = RequestMethod.POST)
    public MessageResponse addTagToPost(@CurrentUser User user,
                                        @PathVariable int postId,
                                        @PathVariable int tagId) throws DataException {
        postService.addTagToPost(user, postId, tagId);
        return new MessageResponse("Add Tag To Post Successful");
    }

    //Remove
    @ResponseBody
    @RequestMapping(value = "/{postId}/tags/{tagId}", method = RequestMethod.DELETE)
    public MessageResponse removeTagFromPost(@CurrentUser User user, 
                                             @PathVariable int postId, 
                                             @PathVariable int tagId) throws DataException {
        postService.removeTagFromPost(user, postId, tagId);
        return new MessageResponse("Remove Tag From Post Successful");
    }

    //GetAll
    @JSON(type = Tag.class, include = "id,title,info,createdTime,updatedTime")
    @RequestMapping(value = "/{postId}/tags", method = RequestMethod.GET)
    public Iterable<Tag> getTagsFromPost(@CurrentUser User user,
                                         @PathVariable int postId) {
        return tagService.getTagsByPostId(user, postId);
    }
}
