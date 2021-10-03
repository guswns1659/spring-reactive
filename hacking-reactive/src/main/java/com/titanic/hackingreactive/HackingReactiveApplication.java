package com.titanic.hackingreactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;
import org.thymeleaf.TemplateEngine;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class HackingReactiveApplication {

    public static void main(String[] args) {
        // TODO(jack.comeback) : 책과 다르게 blockHound가 동작하지 않는다.
        BlockHound.builder()
            .allowBlockingCallsInside(
                TemplateEngine.class.getCanonicalName(), "process"
            );
//        BlockHound.install();

        SpringApplication.run(HackingReactiveApplication.class, args);
    }
}
