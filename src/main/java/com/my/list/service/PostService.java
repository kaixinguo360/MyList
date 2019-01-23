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

    public Post getPost(@NotNull User user, int postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null && post.getUserId() == user.getId()) {
            return post;
        } else {
            logger.info("getPost: Post Not Exist!");
            return null;
        }
    }

    public Iterable<Post> getAllPosts(@NotNull User user) {
        return postRepository.findAllByUserId(user.getId());
    }

    public Iterable<Post> getPostsByTagId(@NotNull User user, int tagId) {
        return postRepository.findAllByUserIdAndTagId(user.getId(), tagId);
    }

    public Iterable<Post> getPostsByTagTitle(@NotNull User user, @NotNull String tagTitle) {
        return postRepository.findAllByUserIdAndTagTitle(user.getId(), tagTitle);
    }

    public Iterable<Post> search(@NotNull User user, String title) {
        if(StringUtils.isEmpty(title))
            return new ArrayList<>();
        return postRepository.findAllByUserIdAndTitleLike(user.getId(), title);
    }

    public boolean addPost(@NotNull User user, @NotNull Post post) {
        try {
            post.setUserId(user.getId());
            postRepository.save(post);
            return true;
        } catch (Exception e) {
            logger.info("addPost: An Error Occur: " + e.getMessage());
            return false;
        }
    }

    public boolean removePost(User user, int postId) {
        try {
            Post post = getPost(user, postId);
            if (post != null) {
                postRepository.deleteById(postId);
                return true;
            } else {
                logger.info("removePost: Post Not Exist");
                return false;
            }
        } catch (Exception e) {
            logger.info("removePost: An Error Occur: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updatePost(@NotNull User user, int postId, @NotNull Post newPost) {
        Post post = getPost(user, postId);
        if (post != null) {
            post.setTitle(newPost.getTitle());
            post.setContent(newPost.getContent());
            return true;
        } else {
            logger.info("updatePostTitle: Post Not Exist!");
            return false;
        }
    }

    @Transactional
    public boolean addTagToPost(@NotNull User user, int postId, int tagId) {
        Post post = getPost(user, postId);
        if (post != null) {
            Tag tag = tagService.getTag(user, tagId);
            if (tag != null) {
                post.getTags().add(tag);
                return true;
            } else {
                logger.info("addTagToPost: Tag Not Exist!");
                return false;
            }
        } else {
            logger.info("addTagToPost: Post Not Exist!");
            return false;
        }
    }

    @Transactional
    public boolean removeTagFromPost(@NotNull User user, int postId, int tagId) {
        Post post = getPost(user, postId);
        if (post != null) {
            Tag tag = tagService.getTag(user, tagId);
            if (tag != null) {
                post.getTags().remove(tag);
                return true;
            } else {
                logger.info("addTagToPost: Tag Not Exist!");
                return false;
            }
        } else {
            logger.info("addTagToPost: Post Not Exist!");
            return false;
        }
    }
}
