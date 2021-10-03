package com.titanic.hackingreactive.chap02;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    /**
     * log() 연산에 의해 출력되는 로그 수준 기본값은 INFO이다. 하지만 log() 메서드의 두번재 인자로 LEVEL을 넘겨주면 원하는 로그 수준으로 출력할 수 있다.
     * 세번째 인자로 리액티브 스트림의 Signal을 넘겨주면 특정 신호에 대한 로그만 출력할 수 있다.
     */
    public Mono<Cart> addToCart(String cartId, String id) {
        return cartRepository.findById(cartId)
            .log(">>>>>><<<<< foundCart")
            .defaultIfEmpty(new Cart(cartId))
            .log("emptyCart")
            .flatMap(cart -> cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(id))
                .findAny()
                .map(cartItem -> {
                    cartItem.increment();
                    return Mono.just(cart);
                })
                .orElseGet(() -> itemRepository.findById(id)
                    .log("fetchedItem")
                    .map(CartItem::new)
                    .log("cartItem")
                    .map(cartItem -> {
                        cart.getCartItems().add(cartItem);
                        return cart;
                    })))
            .log("cartWithAnotherItem")
            .flatMap(cartRepository::save)
            .log("savedCart");

    }
}
