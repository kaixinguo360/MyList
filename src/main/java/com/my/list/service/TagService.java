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

    //Get
    @NotNull
    public Tag getTag(@NotNull User user, int tagId) throws DataException {
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (tag != null && tag.getUserId() == user.getId()) {
            return tag;
        } else {
            logger.info("removeTag: Tag(" + tagId + ") Not Exist");
            throw new DataException("removeTag: Tag(" + tagId + ") Not Exist", ErrorType.NOT_FOUND);
        }
    }

    //GetAll - Item Id
    @NotNull
    public Iterable<Tag> getTagsByItemId(@NotNull User user, int postId) {
        return tagRepository.findAllByUserIdAndItemId(user.getId(), postId);
    }

    //GetAll
    @NotNull
    public Iterable<Tag> getAllTags(@NotNull User user) {
        return tagRepository.findAllByUserId(user.getId());
    }

    //Search
    @NotNull
    public Iterable<Tag> search(@NotNull User user, String title) {
        if(StringUtils.isEmpty(title))
            return new ArrayList<>();
        return tagRepository.findAllByUserIdAndTitleLike(user.getId(), title);
    }

    //Add
    public void addTag(@NotNull User user, @NotNull Tag tag) throws DataException {
        try {
            tag.setUserId(user.getId());
            tagRepository.save(tag);
        } catch (Exception e) {
            logger.info("addTag: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Remove
    public void removeTag(User user, int tagId) throws DataException {
        try {
            getTag(user, tagId);
            tagRepository.deleteById(tagId);
        } catch (DataException e) {
            throw e;
        } catch (Exception e) {
            logger.info("removeTag: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Update
    @Transactional
    public void updateTag(@NotNull User user, int tagId, @NotNull Tag newTag) throws DataException {
        Tag tag = getTag(user, tagId);
        tag.setTitle(newTag.getTitle());
        tag.setInfo(newTag.getInfo());
    }
}
