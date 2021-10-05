package com.titanic.hackingreactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HackingReactiveApplication {

    public static void main(String[] args) {
        // TODO(jack.comeback) : blockingHound는 테스트 단위에서만 활용되도록 한다.
//        BlockHound.builder()
//            .allowBlockingCallsInside(
//                TemplateEngine.class.getCanonicalName(), "process"
//            ).install();

        SpringApplication.run(HackingReactiveApplication.class, args);
    }
}
