package com.titanic.hackingreactive.chap03;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.titanic.hackingreactive.chap01.HomeController;
import com.titanic.hackingreactive.chap02.CartRepository;
import com.titanic.hackingreactive.chap02.InventoryService;
import com.titanic.hackingreactive.chap02.Item;
import com.titanic.hackingreactive.chap02.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(HomeController.class)
public class HomeControllerSliceTest {

    @MockBean
    InventoryService inventoryService;
    @MockBean
    CartRepository cartRepository;
    @MockBean
    ItemRepository itemRepository;
    @Autowired
    private WebTestClient client;

    @Test
    public void homePage() {
        when(itemRepository.findAll())
            .thenReturn(Flux.just(new Item("id", "name1", 0.0)));
        when(cartRepository.findById(anyString()))
            .thenReturn(Mono.empty());

        client.get().uri("/").exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .consumeWith(exchangeResult -> {
                assertThat(exchangeResult.getResponseBody()).contains("action");
            });
    }
}
