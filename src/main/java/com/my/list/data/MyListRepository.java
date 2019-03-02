package com.my.list.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MyListRepository extends CrudRepository<MyList, Integer> {

    Iterable<MyList> findAllByUserId(int userId);

    @Query("SELECT l FROM List l where l.userId = ?1 and l.title like %?2%")
    Iterable<MyList> findAllByUserIdAndTitleLike(int userId, String title);
}
