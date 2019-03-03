package com.my.list.service;

import com.my.list.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    private final Logger logger = LoggerFactory.getLogger(ItemService.class);
    private final ItemRepository itemRepository;
    private final TagService tagService;
    private final MyListService myListService;

    @Autowired
    public ItemService(ItemRepository itemRepository, TagService tagService, MyListService myListService) {
        this.itemRepository = itemRepository;
        this.tagService = tagService;
        this.myListService = myListService;
    }

    //Get
    @NotNull
    public Item get(@NotNull User user, int postId) throws DataException {
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
    public Iterable<Item> getAll(@NotNull User user) {
        return itemRepository.findAllByUserId(user.getId());
    }

    //GetAll - Tag Id
    @NotNull
    public Iterable<Item> getAllByTagId(@NotNull User user, int tagId) {
        return itemRepository.findAllByUserIdAndTagId(user.getId(), tagId);
    }

    //GetAll - List Id
    @NotNull
    public Iterable<Item> getAllByListId(@NotNull User user, int listId) {
        return itemRepository.findAllByUserIdAndListId(user.getId(), listId);
    }

    //Search
    @NotNull
    public Iterable<Item> search(@NotNull User user, String title) {
        if(StringUtils.isEmpty(title))
            return new ArrayList<>();
        return itemRepository.findAllByUserIdAndTitleLike(user.getId(), title);
    }

    //Save
    @Transactional
    public Item save(@NotNull User user, @NotNull Item item) throws DataException {
        try {
            List<Tag> tags = new ArrayList<>();
            for (Tag tag : item.getTags()){
                tags.add(tagService.get(user, tag.getId()));
            }
            item.getTags().clear();
            item.setUserId(user.getId());
            item = itemRepository.save(item);
            item.getTags().addAll(tags);
            return item;
        } catch (Exception e) {
            logger.info("saveItem: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Remove
    @Transactional
    public void remove(User user, int postId) throws DataException {
        try {
            get(user, postId);
            itemRepository.deleteById(postId);
        } catch (DataException e) {
            throw e;
        } catch (Exception e) {
            logger.info("removeItem: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Set - List
    @Transactional
    public void setList(@NotNull User user, int postId, int listId) throws DataException {
        Item item = get(user, postId);
        MyList list = myListService.getList(user, listId);
        item.setList(list);
    }

    //Remove - List
    @Transactional
    public void removeList(@NotNull User user, int postId) throws DataException {
        Item item = get(user, postId);
        item.setList(null);
    }
}
