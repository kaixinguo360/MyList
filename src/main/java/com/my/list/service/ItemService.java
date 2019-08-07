package com.my.list.service;

import com.my.list.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
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
        Item item = itemRepository.findByUserIdAndId(user.getId(), postId);
        if (item != null) {
            return item;
        } else {
            logger.info("getItem: Item(" + postId + ") Not Exist");
            throw new DataException("Item(" + postId + ") Not Exist", ErrorType.NOT_FOUND);
        }
    }

    //GetAll
    @NotNull
    public Iterable<Item> getAll(@NotNull User user, List<Integer> postIds) {
        return itemRepository.findAllByUserIdAndIdIn(user.getId(), postIds);
    }

    //GetAll
    @NotNull
    public Iterable<Item> getAll(@NotNull User user) {
        return itemRepository.findAllByUserId(user.getId());
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
    public void remove(User user, int itemId) throws DataException {
        try {
            get(user, itemId);
            itemRepository.deleteById(itemId);
        } catch (DataException e) {
            throw e;
        } catch (Exception e) {
            logger.info("removeItem: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Remove All
    @Transactional
    public void removeAll(User user, List<Integer> ids) throws DataException {
        try {
            itemRepository.deleteAllByUserIdAndIdIn(user.getId(), ids);
        } catch (Exception e) {
            logger.info("removeItem: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    // ---------- List ---------- //

    //GetAll By List
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
            itemRepository.setListByUserIdAndIdIn(list, user.getId(), itemIds);
        } else {
            itemRepository.setListByUserIdAndIdIn(null, user.getId(), itemIds);
        }
    }
    
    // ---------- Tag ---------- //

    //GetAll By Tag
    @NotNull
    public Iterable<Item> getAllByTagId(@NotNull User user, Integer tagId) {
        if (tagId != null) {
            return itemRepository.findAllByUserIdAndTagId(user.getId(), tagId);
        } else {
            return itemRepository.findAllByUserIdAndTagsIsNull(user.getId());
        }
    }

    //Add Tags
    @Transactional
    public void addTags(@NotNull User user, List<Integer> itemIds, List<Integer> tagIds, boolean isClear) {
        Iterable<Item> items = this.getAll(user, itemIds);
        Iterable<Tag> tags = this.tagService.getAll(user, tagIds);
        for (Item item : items) {
            if (isClear) item.getTags().clear();
            for (Tag tag : tags) {
                item.getTags().add(tag);
            }
        }
        itemRepository.saveAll(items);
    }

    //Search By Tags
    @NotNull
    public Iterable<Item> searchByTagIds(@NotNull User user, List<Integer> andTagIds, List<Integer> orTagIds, List<Integer> notTagIds) {
        Specification querySpecification = (Specification<Item>) (root, query, cb) -> {
            query.distinct(true);
            Predicate p = cb.equal(root.get("userId"), user.getId());
            Predicate andPredicate = null;
            if (andTagIds != null) {
                for (int tagId : andTagIds) {
                    if (andPredicate == null) {
                        andPredicate = cb.equal(root.join("tags").get("id"), tagId);
                    } else {
                        andPredicate = cb.and(andPredicate, cb.equal(root.join("tags").get("id"), tagId));
                    }
                }
                p = cb.and(p, andPredicate);
            }
            if (orTagIds != null) {
                Predicate orPredicate = root.join("tags").get("id").in(orTagIds);
                if (andPredicate != null) {
                    orPredicate = cb.or(orPredicate, andPredicate);
                }
                p = cb.and(p, orPredicate);
            }
            if (notTagIds != null) {
                Subquery<Item> subquery = query.subquery(Item.class);
                Root<Item> subRoot = subquery.from(Item.class);
                subquery = subquery.select(subRoot.get("id")).where(
                    subRoot.join("tags").get("id").in(notTagIds)
                );
                p = cb.and(p, root.get("id").in(subquery).not());
            }
            return p;
        };
        return (List<Item>) this.itemRepository.findAll(querySpecification);
    }
}
