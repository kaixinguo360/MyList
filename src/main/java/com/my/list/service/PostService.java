package com.my.list.service;

import com.my.list.data.Post;
import com.my.list.data.PostRepository;
import com.my.list.data.Tag;
import com.my.list.data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Service
public class PostService {

    private final Logger logger = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;
    private final TagService tagService;

    @Autowired
    public PostService(PostRepository postRepository, TagService tagService) {
        this.postRepository = postRepository;
        this.tagService = tagService;
    }

    //Get
    @NotNull
    public Post getPost(@NotNull User user, int postId) throws DataException {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null && post.getUserId() == user.getId()) {
            return post;
        } else {
            logger.info("getPost: Post(" + postId + ") Not Exist");
            throw new DataException("Post(" + postId + ") Not Exist", ErrorType.NOT_FOUND);
        }
    }

    //GetAll
    @NotNull
    public Iterable<Post> getAllPosts(@NotNull User user) {
        return postRepository.findAllByUserId(user.getId());
    }

    //GetAll - Tag Id
    @NotNull
    public Iterable<Post> getPostsByTagId(@NotNull User user, int tagId) {
        return postRepository.findAllByUserIdAndTagId(user.getId(), tagId);
    }

    //Search
    @NotNull
    public Iterable<Post> search(@NotNull User user, String title) {
        if(StringUtils.isEmpty(title))
            return new ArrayList<>();
        return postRepository.findAllByUserIdAndTitleLike(user.getId(), title);
    }

    //Add
    public void addPost(@NotNull User user, @NotNull Post post) throws DataException {
        try {
            post.setUserId(user.getId());
            postRepository.save(post);
        } catch (Exception e) {
            logger.info("addPost: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Remove
    public void removePost(User user, int postId) throws DataException {
        try {
            getPost(user, postId);
            postRepository.deleteById(postId);
        } catch (DataException e) {
            throw e;
        } catch (Exception e) {
            logger.info("removePost: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Update
    @Transactional
    public void updatePost(@NotNull User user, int postId, @NotNull Post newPost) throws DataException {
        Post post = getPost(user, postId);
        post.setTitle(newPost.getTitle());
        post.setContent(newPost.getContent());
    }

    //Add - Tag
    @Transactional
    public void addTagToPost(@NotNull User user, int postId, int tagId) throws DataException {
        Post post = getPost(user, postId);
        Tag tag = tagService.getTag(user, tagId);
        post.getTags().add(tag);
    }

    //Remove - Tag
    @Transactional
    public void removeTagFromPost(@NotNull User user, int postId, int tagId) throws DataException {
        Post post = getPost(user, postId);
        Tag tag = tagService.getTag(user, tagId);
        post.getTags().remove(tag);
    }
}
