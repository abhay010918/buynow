package com.buynow.service;


import com.buynow.entity.CartItem;

import java.util.List;

public interface CartService {

    CartItem addToCart(Long userId, Long productId, int quantity);

    List<CartItem> getCartItem(Long userId);

    void updateQuantity(Long cartItem, int quantity);

    void removeItem(Long cartItemId);

    void clearCart(Long userID);
}
