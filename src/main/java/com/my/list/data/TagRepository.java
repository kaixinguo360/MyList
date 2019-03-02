package com.my.list.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Integer> {

    Iterable<Tag> findAllByUserId(int userId);

    @Query("SELECT t FROM Tag t where t.userId = ?1 and t.title like %?2%")
    Iterable<Tag> findAllByUserIdAndTitleLike(int userId, String title);

    @Query("SELECT t FROM Tag t JOIN t.items its where t.userId = ?1 and its.userId = ?1 and its.id = ?2")
    Iterable<Tag> findAllByUserIdAndItemId(int useId, int postId);

    @Query("SELECT t FROM Tag t JOIN t.items its where t.userId = ?1 and its.userId = ?1 and its.title = ?2")
    Iterable<Tag> findAllByUserIdAndItemTitle(int useId, String postTitle);
}
