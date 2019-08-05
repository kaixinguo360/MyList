package com.my.list.data;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Integer> {

    Item findByUserIdAndId(int userId, int id);

    Iterable<Item> findAllByUserId(int userId);

    Iterable<Item> findAllByUserIdAndIdIn(int userId, List<Integer> id);

    @Query("SELECT i FROM Item i where i.userId = ?1 and i.title like %?2%")
    Iterable<Item> findAllByUserIdAndTitleLike(int userId, String title);

    @Query("SELECT i FROM Item i where i.userId = ?1 and i.list.id = ?2")
    Iterable<Item> findAllByUserIdAndListId(int useId, int listId);

    Iterable<Item> findAllByUserIdAndListIsNull(int useId);

    @Query("SELECT i FROM Item i JOIN i.tags ts where i.userId = ?1 and ts.userId = ?1 and ts.id = ?2")
    Iterable<Item> findAllByUserIdAndTagId(int useId, int tagId);

    Iterable<Item> findAllByUserIdAndTagsIsNull(int userId);

    @Query("SELECT i FROM Item i JOIN i.tags ts where i.userId = ?1 and ts.userId = ?1 and ts.title = ?2")
    Iterable<Item> findAllByUserIdAndTagTitle(int useId, String tagTitle);

    @Modifying
    @Query("UPDATE Item i SET i.list = ?1 where i.userId = ?2 and i.id in ?3")
    void setListByUserIdAndIdIn(MyList list, int useId, List<Integer> itemIds);
    
    void deleteAllByUserIdAndIdIn(int userId, Collection<Integer> id);
}
