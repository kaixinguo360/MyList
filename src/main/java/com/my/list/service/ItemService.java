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
    public ItemService(ItemRepository itemRepository,
                       TagService tagService,
                       MyListService myListService) {
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
    public Iterable<Item> getAllByTagId(@NotNull User user, Integer tagId) {
        if (tagId != null) {
            return itemRepository.findAllByUserIdAndTagId(user.getId(), tagId);
        } else {
            return itemRepository.findAllByUserIdAndTagsIsNull(user.getId());
        }
    }

    //GetAll - List Id
    @NotNull
    public Iterable<Item> getAllByListId(@NotNull User user, Integer listId) {
        if (listId != null) {
            return itemRepository.findAllByUserIdAndListId(user.getId(), listId);
        } else {
            return itemRepository.findAllByUserIdAndListIsNull(user.getId());
        }
    }

    //Set List
    @Transactional
    public void setList(@NotNull User user, List<Integer> itemIds, Integer listId) throws DataException {
        if (listId != null) {
            MyList list = this.myListService.get(user, listId);
            itemRepository.setListByUserIdAndIds(list, user.getId(), itemIds);
        } else {
            itemRepository.setListByUserIdAndIds(null, user.getId(), itemIds);
        }
    }

    //Search
    @NotNull
    public Iterable<Item> search(@NotNull User user, String title) {
        if(StringUtils.isEmpty(title))
            return new ArrayList<>();
        return itemRepository.findAllByUserIdAndTitleLike(user.getId(), title);
    }

    //Add
    @Transactional
    public Item add(@NotNull User user, @NotNull Item item) throws DataException {
        try {
            item.setId(0);
            return save(user, item);
        } catch (Exception e) {
            logger.info("addItem: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Update
    @Transactional
    public Item update(@NotNull User user, @NotNull Item item) throws DataException {
        try {
            get(user, item.getId());
            return save(user, item);
        } catch (Exception e) {
            logger.info("updateItem: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Save
    private Item save(User user, final Item item) throws DataException {

        MyList list = null;
        if (item.getList() != null) {
            list = myListService.get(user, item.getList().getId());
        }

        List<Tag> tags = new ArrayList<>();
        for (Tag tag : item.getTags()){
            tags.add(tagService.get(user, tag.getId()));
        }

        item.getTexts().forEach(c -> {
            c.setId(0);
            c.setItem(item);
        });
        item.getImages().forEach(c -> {
            c.setId(0);
            c.setItem(item);
        });
        item.getMusics().forEach(c -> {
            c.setId(0);
            c.setItem(item);
        });
        item.getVideos().forEach(c -> {
            c.setId(0);
            c.setItem(item);
        });
        item.getLinks().forEach(c -> {
            c.setId(0);
            c.setItem(item);
        });

        item.getTags().clear();
        item.setUserId(user.getId());

        Item item1 = itemRepository.save(item);
        item1.getTags().addAll(tags);
        item1.setList(list);

        return item1;
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
}
