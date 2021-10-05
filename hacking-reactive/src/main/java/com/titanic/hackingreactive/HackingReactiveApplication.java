package com.titanic.hackingreactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;
import org.thymeleaf.TemplateEngine;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class HackingReactiveApplication {

    public static void main(String[] args) {
        BlockHound.builder()
            .allowBlockingCallsInside(
                TemplateEngine.class.getCanonicalName(), "process"
            ).install();

        SpringApplication.run(HackingReactiveApplication.class, args);
    }
}
