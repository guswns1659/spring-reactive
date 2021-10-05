package com.titanic.hackingreactive.chap03;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.titanic.hackingreactive.chap02.Cart;
import com.titanic.hackingreactive.chap02.CartItem;
import com.titanic.hackingreactive.chap02.CartRepository;
import com.titanic.hackingreactive.chap02.InventoryService;
import com.titanic.hackingreactive.chap02.Item;
import com.titanic.hackingreactive.chap02.ItemRepository;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * MockitoExtension.class의 @Mock을 이용할 때는 정상적으로 처리가 안되는 느낌.
 * SpringExtension.class 사용하기
 */
@ExtendWith(value = {SpringExtension.class})
public class InventoryServiceTest {

    private InventoryService inventoryService;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private CartRepository cartRepository;

    @BeforeEach
    public void setUp() {
        // test data
        Item sampleItem = new Item("item1", "alf Tv", 19.99);
        CartItem sampleCartItem = new CartItem(sampleItem);
        Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

        // mocking
        Mockito.when(cartRepository.findById(anyString())).thenReturn(Mono.empty());
        Mockito.when(itemRepository.findById(anyString())).thenReturn(Mono.just(sampleItem));
        Mockito.when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(sampleCart));

        inventoryService = new InventoryService(itemRepository, cartRepository);
    }

    @Test
    void addItemToEmptyCartShouldProduceOneCartItem() {

        inventoryService.addToCart("My Cart", "item1")
            .as(StepVerifier::create) // 테스트 기능을 전담하는 리액터 타입 핸들러 생성, 여기서 구독 시작
            .expectNextMatches(cart -> {
                assertThat(cart.getCartItems()).extracting(CartItem::getQuantity)
                    .containsExactlyInAnyOrder(1); // 순서에 상관없이 1이란 값이 있는지 확인

                assertThat(cart.getCartItems()).extracting(CartItem::getItem)
                    .containsExactly(new Item("item1", "alf Tv", 19.99)); // 순서와 값이 정확한지 확인

                return true;
            })
            .verifyComplete(); // onComplete 시그널 받았는지 확인
    }
}
