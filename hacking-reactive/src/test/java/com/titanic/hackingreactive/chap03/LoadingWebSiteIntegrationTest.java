package com.titanic.hackingreactive.chap03;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // @SpringBootApplication 어노테이션이 붙은 클래스를 찾아서 내장 컨테이너를 실행한다.
@AutoConfigureWebTestClient // WebTestClient 인스턴스를 생성
public class LoadingWebSiteIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void homeUriTest() {
        webTestClient.get().uri("/").exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.TEXT_HTML)
            .expectBody(String.class)
            .consumeWith(exchangeResult -> assertThat(exchangeResult.getResponseBody()).contains("<h2>Inventory Management</h2>"));
    }
}
