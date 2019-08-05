package com.my.list.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MyListRepository extends CrudRepository<MyList, Integer> {

    MyList findByUserIdAndId(int userId, int id);

    Iterable<MyList> findAllByUserId(int userId);

    Iterable<MyList> findAllByUserIdAndIdIn(int userId, List<Integer> id);

    @Query("SELECT l FROM List l where l.userId = ?1 and l.title like %?2%")
    Iterable<MyList> findAllByUserIdAndTitleLike(int userId, String title);
}
