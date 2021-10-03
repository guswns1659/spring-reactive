package com.titanic.hackingreactive.chap02;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class RepositoryDatabaseLoader {

    @Bean
    CommandLineRunner initialize(MongoOperations mongo) {
        return args -> {
            mongo.save(new Item("alf alarm clock", 19.99));
            mongo.save(new Item("smurf Tv", 24.99));
        };
    }
}
