package com.titanic.hackingreactive.chap01;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class KitchenService {

    Flux<Dish> getDishes() {
        return Flux.<Dish> generate(sink -> sink.next(randomDish())).delayElements(Duration.ofMillis(250));
    }

    private List<Dish> menu = Arrays.asList(
        new Dish("Sesame"),
        new Dish("Lo mein noodles"),
        new Dish("sweet")
    );

    private Dish randomDish() {
        return menu.get(new Random().nextInt(menu.size()));
    }
}
