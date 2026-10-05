package com.rahulshop.backend.service;

import com.rahulshop.backend.entity.CartItem;
import com.rahulshop.backend.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    // Get logged-in user's cart
    public List<CartItem> getCartItemsByUser(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    // Add item to cart
    public CartItem addToCart(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    // Update quantity
    public CartItem updateQuantity(Long id, int quantity, Long userId) {

        CartItem cartItem = cartItemRepository
                .findByIdAndUserId(id, userId)
                .orElse(null);

        if (cartItem == null) {
            return null;
        }

        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    // Remove item from user's cart
    public void removeFromCart(Long id, Long userId) {

        Optional<CartItem> cartItem =
                cartItemRepository.findByIdAndUserId(id, userId);

        if (cartItem.isPresent()) {
            cartItemRepository.delete(cartItem.get());
        }
    }

    // Clear only logged-in user's cart
    public void clearCart(Long userId) {

        List<CartItem> cartItems =
                cartItemRepository.findByUserId(userId);

        cartItemRepository.deleteAll(cartItems);
    }
}