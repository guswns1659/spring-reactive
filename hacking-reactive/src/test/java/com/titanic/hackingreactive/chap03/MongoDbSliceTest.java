package com.titanic.hackingreactive.chap03;

import static org.assertj.core.api.Assertions.assertThat;

import com.titanic.hackingreactive.chap02.Item;
import com.titanic.hackingreactive.chap02.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

@DataMongoTest
public class MongoDbSliceTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void itemRepositorySavesItems() {
        // given
        Item sampleItem = new Item("id","name", 1.99);
        Item actualItem = new Item("id","name", 1.99);

        // when, then
        itemRepository.save(sampleItem)
            .as(StepVerifier::create)
            .expectNextMatches(item -> {
                assertThat(item).usingRecursiveComparison().isEqualTo(actualItem);
                return true;
            })
            .verifyComplete();
    }
}
