package com.my.list.service;

import com.my.list.data.Item;
import com.my.list.data.ItemRepository;
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
public class ItemService {

    private final Logger logger = LoggerFactory.getLogger(ItemService.class);
    private final ItemRepository itemRepository;
    private final TagService tagService;

    @Autowired
    public ItemService(ItemRepository itemRepository, TagService tagService) {
        this.itemRepository = itemRepository;
        this.tagService = tagService;
    }

    //Get
    @NotNull
    public Item getItem(@NotNull User user, int postId) throws DataException {
        Item item = itemRepository.findById(postId).orElse(null);
        if (item != null && item.getUserId() == user.getId()) {
            return item;
        } else {
            logger.info("getItem: Item(" + postId + ") Not Exist");
            throw new DataException("Item(" + postId + ") Not Exist", ErrorType.NOT_FOUND);
        }
    }

    //GetAll
    @NotNull
    public Iterable<Item> getAllItems(@NotNull User user) {
        return itemRepository.findAllByUserId(user.getId());
    }

    //GetAll - Tag Id
    @NotNull
    public Iterable<Item> getItemsByTagId(@NotNull User user, int tagId) {
        return itemRepository.findAllByUserIdAndTagId(user.getId(), tagId);
    }

    //Search
    @NotNull
    public Iterable<Item> search(@NotNull User user, String title) {
        if(StringUtils.isEmpty(title))
            return new ArrayList<>();
        return itemRepository.findAllByUserIdAndTitleLike(user.getId(), title);
    }

    //Add
    public void addItem(@NotNull User user, @NotNull Item item) throws DataException {
        try {
            item.setUserId(user.getId());
            itemRepository.save(item);
        } catch (Exception e) {
            logger.info("addItem: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Remove
    public void removeItem(User user, int postId) throws DataException {
        try {
            getItem(user, postId);
            itemRepository.deleteById(postId);
        } catch (DataException e) {
            throw e;
        } catch (Exception e) {
            logger.info("removeItem: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Update
    @Transactional
    public void updateItem(@NotNull User user, int postId, @NotNull Item newItem) throws DataException {
        Item item = getItem(user, postId);
        item.setTitle(newItem.getTitle());
        item.setInfo(newItem.getInfo());
    }

    //Add - Tag
    @Transactional
    public void addTagToItem(@NotNull User user, int postId, int tagId) throws DataException {
        Item item = getItem(user, postId);
        Tag tag = tagService.getTag(user, tagId);
        item.getTags().add(tag);
    }

    //Remove - Tag
    @Transactional
    public void removeTagFromItem(@NotNull User user, int postId, int tagId) throws DataException {
        Item item = getItem(user, postId);
        Tag tag = tagService.getTag(user, tagId);
        item.getTags().remove(tag);
    }
}
