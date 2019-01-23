package com.my.list.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Integer> {

    Iterable<Post> findAllByUserId(int userId);

    @Query("SELECT p FROM Post p where p.userId = ?1 and p.title like %?2%")
    Iterable<Post> findAllByUserIdAndTitleLike(int userId, String title);

    @Query("SELECT p FROM Post p JOIN p.tags ts where p.userId = ?1 and ts.userId = ?1 and ts.id = ?2")
    Iterable<Post> findAllByUserIdAndTagId(int useId, int tagId);

    @Query("SELECT p FROM Post p JOIN p.tags ts where p.userId = ?1 and ts.userId = ?1 and ts.title = ?2")
    Iterable<Post> findAllByUserIdAndTagTitle(int useId, String tagTitle);
}
