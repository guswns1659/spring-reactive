package com.titanic.hackingreactive.chap02;

import org.springframework.data.repository.CrudRepository;

public interface BlockingItemRepository extends CrudRepository<Item, String> {

}
