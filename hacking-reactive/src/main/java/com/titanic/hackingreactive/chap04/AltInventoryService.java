package com.titanic.hackingreactive.chap04;

import com.titanic.hackingreactive.chap02.Cart;
import com.titanic.hackingreactive.chap02.CartItem;
import com.titanic.hackingreactive.chap02.CartRepository;
import com.titanic.hackingreactive.chap02.Item;
import com.titanic.hackingreactive.chap02.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Service
public class AltInventoryService {

    private ItemRepository itemRepository;

    private CartRepository cartRepository;

    public AltInventoryService(ItemRepository repository, CartRepository cartRepository) {
        this.itemRepository = repository;
        this.cartRepository = cartRepository;
    }

    public Mono<Cart> getCart(String cartId) {
        return this.cartRepository.findById(cartId);
    }

    public Flux<Item> getInventory() {
        return this.itemRepository.findAll();
    }

    Mono<Item> saveItem(Item newItem) {
        return this.itemRepository.save(newItem);
    }

    Mono<Void> deleteItem(String id) {
        return this.itemRepository.deleteById(id);
    }

    // tag::blocking[]
    public Mono<Cart> addItemToCart(String cartId, String itemId) {
        Cart myCart = this.cartRepository.findById(cartId) //
            .defaultIfEmpty(new Cart(cartId)) //
            .block();

        return myCart.getCartItems().stream() //
            .filter(cartItem -> cartItem.getItem().getId().equals(itemId)) //
            .findAny() //
            .map(cartItem -> {
                cartItem.increment();
                return Mono.just(myCart);
            }) //
            .orElseGet(() -> this.itemRepository.findById(itemId) //
                .map(item -> new CartItem(item)) //
                .map(cartItem -> {
                    myCart.getCartItems().add(cartItem);
                    return myCart;
                })) //
            .flatMap(cart -> this.cartRepository.save(cart));
    }
    // end::blocking[]
}
// end::code[]
