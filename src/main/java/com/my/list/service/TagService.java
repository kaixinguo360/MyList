package com.my.list.service;

import com.my.list.data.Tag;
import com.my.list.data.TagRepository;
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
public class TagService {

    private final Logger logger = LoggerFactory.getLogger(TagService.class);
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag getTag(@NotNull User user, int tagId) {
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (tag != null && tag.getUserId() == user.getId()) {
            return tag;
        } else {
            logger.info("getTag: Tag Not Exist!");
            return null;
        }
    }

    public Iterable<Tag> getTagByPostId(@NotNull User user, int postId) {
        return tagRepository.findAllByUserIdAndPostId(user.getId(), postId);
    }

    public Iterable<Tag> getTagByPostTitle(@NotNull User user, @NotNull String postTitle) {
        return tagRepository.findAllByUserIdAndPostTitle(user.getId(), postTitle);
    }

    public Iterable<Tag> getAllTags(@NotNull User user) {
        return tagRepository.findAllByUserId(user.getId());
    }

    public Iterable<Tag> search(@NotNull User user, String title) {
        if(StringUtils.isEmpty(title))
            return new ArrayList<>();
        return tagRepository.findAllByUserIdAndTitleLike(user.getId(), title);
    }

    public boolean addTag(@NotNull User user, @NotNull Tag tag) {
        try {
            tag.setUserId(user.getId());
            tagRepository.save(tag);
            return true;
        } catch (Exception e) {
            logger.info("addTag: An Error Occur: " + e.getMessage());
            return false;
        }
    }

    public boolean removeTag(User user, int tagId) {
        try {
            Tag tag = getTag(user, tagId);
            if (tag != null) {
                tagRepository.deleteById(tagId);
                return true;
            } else {
                logger.info("removeTag: Tag Not Exist");
                return false;
            }
        } catch (Exception e) {
            logger.info("removeTag: An Error Occur: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateTag(@NotNull User user, int tagId, @NotNull Tag newTag) {
        Tag tag = getTag(user, tagId);
        if (tag != null) {
            tag.setTitle(newTag.getTitle());
            tag.setInfo(newTag.getInfo());
            return true;
        } else {
            logger.info("updateTagTitle: Tag Not Exist!");
            return false;
        }
    }
}
