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
    Flux<ExampleItem> findByNameAndDescriptionUsingExample(String name, String description, boolean useAnd) {
        ExampleItem exampleItem = new ExampleItem(name, description, 0.0);

        ExampleMatcher matcher = (useAnd ? ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny())
            .withStringMatcher(StringMatcher.CONTAINING)
            .withIgnoreCase()
            .withIgnorePaths("price");

        Example<ExampleItem> probe = Example.of(exampleItem, matcher);
        return itemByExampleRepository.findAll(probe);
    }

    /** TODO(jack.comeback) : 들어오는 값이 어떤 필드인지 모르는데 어떻게 객체를 만들어서 ExampleMatcher에 넘기지?
     * 사용자가 입력한 값을 모든 필드에 대해 부분 일치, 대소문자 불문 검색 쿼리
     */
    Flux<ExampleItem> findBySpecificValueOnAllFieldsUsingExample(String field) {
        return Flux.just(new ExampleItem());
    };
}
