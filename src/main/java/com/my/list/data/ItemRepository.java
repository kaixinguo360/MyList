package com.my.list.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Integer> {

    Iterable<Item> findAllByUserId(int userId);

    @Query("SELECT i FROM Item i where i.userId = ?1 and i.title like %?2%")
    Iterable<Item> findAllByUserIdAndTitleLike(int userId, String title);

    @Query("SELECT i FROM Item i where i.userId = ?1 and i.list.id = ?2")
    Iterable<Item> findAllByUserIdAndListId(int useId, int listId);

    @Query("SELECT i FROM Item i JOIN i.tags ts where i.userId = ?1 and ts.userId = ?1 and ts.id = ?2")
    Iterable<Item> findAllByUserIdAndTagId(int useId, int tagId);

    @Query("SELECT i FROM Item i JOIN i.tags ts where i.userId = ?1 and ts.userId = ?1 and ts.title = ?2")
    Iterable<Item> findAllByUserIdAndTagTitle(int useId, String tagTitle);
}
