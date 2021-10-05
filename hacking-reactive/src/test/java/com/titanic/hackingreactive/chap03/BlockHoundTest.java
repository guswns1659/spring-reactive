package com.titanic.hackingreactive.chap03;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BlockHoundTest {

    @Test
    @DisplayName("실제 테스트는 아래와 같이 verifyComplete로 검증해야한다. 블로킹 코드가 없을 거라고 예상하고 테스트를 짰기 때문이다.")
    void threadSleepIsBlockingCall() {
        Mono.delay(Duration.ofSeconds(1)) // <1>
            .flatMap(tick -> {
                // TODO(jack.comeback) : 여기서 검증할 클래스를 넣는다.
                try {
                    Thread.sleep(10); // <2>
                    return Mono.just(true);
                } catch (InterruptedException e) {
                    return Mono.error(e);
                }
            }) //
            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    @DisplayName("실제 개발 시에는 이런 식으로 테스트 코드를 짤 필요가 없다. 블로킹 코드가 있을 때는 에러로 검출되어야 한다.")
    void threadSleepIsBlockingCall_success() {
        Mono.delay(Duration.ofSeconds(1))
            .flatMap(tick -> {
                try {
                    Thread.sleep(10);
                    return Mono.just(true);
                } catch (InterruptedException e) {
                    return Mono.error(e);
                }
            })
            .as(StepVerifier::create)
            .verifyErrorMatches(throwable ->{
                assertThat(throwable.getMessage()).contains("Blocking call");
                return true;
            });
    }
}
