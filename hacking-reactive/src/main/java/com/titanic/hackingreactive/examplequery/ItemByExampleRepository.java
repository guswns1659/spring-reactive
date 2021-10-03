package com.titanic.hackingreactive.examplequery;

import com.titanic.hackingreactive.chap02.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface ItemByExampleRepository extends ReactiveMongoRepository<ExampleItem, String>, ReactiveQueryByExampleExecutor<ExampleItem> {

}
