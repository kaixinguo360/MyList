package com.my.list.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Integer> {

    Tag findByUserIdAndId(int userId, int id);

    Iterable<Tag> findAllByUserId(int userId);

    Iterable<Tag> findAllByUserIdAndIdIn(int userId, List<Integer> id);

    @Query("SELECT t FROM Tag t where t.userId = ?1 and t.title like %?2%")
    Iterable<Tag> findAllByUserIdAndTitleLike(int userId, String title);
}
