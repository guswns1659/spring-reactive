package com.titanic.hackingreactive.examplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ItemQueryRepository {

    private final ItemByExampleRepository itemByExampleRepository;

    /**
     * Example using ExampleMatcher handling for dynamic query
     * params : name, description
     */
    Flux<ExampleItem> searchByExample(String name, String description, boolean useAnd) {
        ExampleItem exampleItem = new ExampleItem(name, description, 0.0);

        ExampleMatcher matcher = (useAnd ? ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny())
            .withStringMatcher(StringMatcher.CONTAINING)
            .withIgnoreCase()
            .withIgnorePaths("price");

        Example<ExampleItem> probe = Example.of(exampleItem, matcher);
        return itemByExampleRepository.findAll(probe);
    }


}
