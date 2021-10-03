package com.titanic.hackingreactive.chap01;

import com.titanic.hackingreactive.chap02.Cart;
import com.titanic.hackingreactive.chap02.CartItem;
import com.titanic.hackingreactive.chap02.CartRepository;
import com.titanic.hackingreactive.chap02.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    @GetMapping
    Mono<Rendering> home() {
        return Mono.just(Rendering.view("home.html")
            .modelAttribute("items", itemRepository.findAll())
            .modelAttribute("cart", cartRepository.findById("My Cart").defaultIfEmpty(new Cart("My Cart")))
            .build());
    }

    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return cartRepository.findById("My Cart")
            .defaultIfEmpty(new Cart("My Cart"))
            .flatMap(cart -> cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(id))
                .findAny()
                .map(cartItem -> {
                    cartItem.increment();
                    return Mono.just(cart);
                })
                .orElseGet(() -> itemRepository.findById(id)
                    .map(CartItem::new)
                    .map(cartItem -> {
                        cart.getCartItems().add(cartItem);
                        return cart;
                    })))
            .flatMap(cartRepository::save)
            .thenReturn("redirect:/");
    }
}
