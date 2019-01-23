package com.my.list.data;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    boolean existsByName(String name);

    User getByName(String name);
}
