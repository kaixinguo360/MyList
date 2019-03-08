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
    public Tag get(@NotNull User user, int tagId) throws DataException {
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (tag != null && tag.getUserId() == user.getId()) {
            return tag;
        } else {
            logger.info("getTag: Tag(" + tagId + ") Not Exist");
            throw new DataException("Tag(" + tagId + ") Not Exist", ErrorType.NOT_FOUND);
        }
    }

    //GetAll
    @NotNull
    public Iterable<Tag> getAll(@NotNull User user) {
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
    @Transactional
    public Tag add(@NotNull User user, @NotNull Tag tag) throws DataException {
        try {
            tag.setId(0);
            return save(user, tag);
        } catch (Exception e) {
            logger.info("addTag: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Update
    @Transactional
    public Tag update(@NotNull User user, @NotNull Tag tag) throws DataException {
        try {
            get(user, tag.getId());
            return save(user, tag);
        } catch (Exception e) {
            logger.info("updateTag: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Save
    private Tag save(User user, Tag tag) {
        tag.setUserId(user.getId());
        return tagRepository.save(tag);
    }

    //Remove
    @Transactional
    public void remove(User user, int tagId) throws DataException {
        try {
            Tag tag = get(user, tagId);
            tag.getItems().forEach(item -> item.getTags().remove(tag));
            tagRepository.deleteById(tagId);
        } catch (DataException e) {
            throw e;
        } catch (Exception e) {
            logger.info("removeTag: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }
}
