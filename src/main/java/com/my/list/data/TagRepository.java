package com.my.list.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Integer> {

    Iterable<Tag> findAllByUserId(int userId);

    @Query("SELECT t FROM Tag t where t.userId = ?1 and t.title like %?2%")
    Iterable<Tag> findAllByUserIdAndTitleLike(int userId, String title);

    @Query("SELECT t FROM Tag t JOIN t.posts ps where t.userId = ?1 and ps.userId = ?1 and ps.id = ?2")
    Iterable<Tag> findAllByUserIdAndPostId(int useId, int postId);

    @Query("SELECT t FROM Tag t JOIN t.posts ps where t.userId = ?1 and ps.userId = ?1 and ps.title = ?2")
    Iterable<Tag> findAllByUserIdAndPostTitle(int useId, String postTitle);
}
