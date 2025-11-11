package com.buynow.controller;

import com.buynow.dto.CartItemResponse;
import com.buynow.entity.CartItem;
import com.buynow.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public CartItemResponse addToCart(@RequestParam Long userId,
                                      @RequestParam Long productId,
                                      @RequestParam int quantity) {
        return cartService.addToCart(userId, productId, quantity);
    }

    @GetMapping
    public List<CartItem> getCart(@RequestParam Long userId) {
        return cartService.getCartItem(userId);
    }

    @PutMapping("/update/{cartItemId}")
    public void updateQuantity(@PathVariable Long cartItemId,
                               @RequestParam int quantity) {
        cartService.updateQuantity(cartItemId, quantity);
    }

    @DeleteMapping("/remove/{cartItemId}")
    public void removeItem(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
    }

    @DeleteMapping("/clear")
    public void clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
    }
}
