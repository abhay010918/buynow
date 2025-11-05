package com.buynow.service.impl;

import com.buynow.entity.CartItem;
import com.buynow.entity.Product;
import com.buynow.entity.User;
import com.buynow.exeptions.ResourceNotFoundException;
import com.buynow.repository.CartItemRepository;
import com.buynow.repository.ProductRepository;
import com.buynow.repository.UserRepository;
import com.buynow.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartItemRepository cartItemRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CartItem addToCart(Long userId, Long productId, int quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));

        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(quantity);

        return cartItemRepository.save(item);
    }

    @Override
    public List<CartItem> getCartItem(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public void updateQuantity(Long cartItemId, int quantity) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("item not found"));

        item.setQuantity(quantity);
    }

    @Override
    public void removeItem(Long cartItemId) {

        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void clearCart(Long userID) {

        cartItemRepository.deleteByUserId(userID);
    }
}
