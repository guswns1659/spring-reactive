package com.titanic.hackingreactive.chap03;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.titanic.hackingreactive.chap02.Cart;
import com.titanic.hackingreactive.chap02.CartItem;
import com.titanic.hackingreactive.chap02.CartRepository;
import com.titanic.hackingreactive.chap02.Item;
import com.titanic.hackingreactive.chap02.ItemRepository;
import com.titanic.hackingreactive.chap04.AltInventoryService;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author Greg Turnquist
 */
// tag::1[]
@ExtendWith(SpringExtension.class) // <1>
class BlockHoundIntegrationTest {

    AltInventoryService inventoryService; // <2>

    @MockBean
    ItemRepository itemRepository; // <3>
    @MockBean
    CartRepository cartRepository;
    // end::1[]

    // tag::2[]
    @BeforeEach
    void setUp() {
        // Define test data <1>

        Item sampleItem = new Item("item1", "Alf TV tray", 19.99);
        CartItem sampleCartItem = new CartItem(sampleItem);
        Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

        // Define mock interactions provided
        // by your collaborators <2>

        when(cartRepository.findById(anyString())) //
            .thenReturn(Mono.<Cart> empty().hide()); // <3>

        when(itemRepository.findById(anyString())).thenReturn(Mono.just(sampleItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(sampleCart));

        inventoryService = new AltInventoryService(itemRepository, cartRepository);
    }

    @Test
    @DisplayName("실제로는 verifyComplete를 통해 검증해야한다.")
    void blockHoundShouldTrapBlockingCall() { //
        Mono.delay(Duration.ofSeconds(1)) // <1>
            .flatMap(tick -> inventoryService.addItemToCart("My Cart", "item1")) // <2>
            .as(StepVerifier::create) // <3>
            .verifyErrorSatisfies(throwable -> { // <4>
                assertThat(throwable).hasMessageContaining( //
                    "block()/blockFirst()/blockLast() are blocking");
            });
    }
}
