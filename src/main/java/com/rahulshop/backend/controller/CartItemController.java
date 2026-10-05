package com.rahulshop.backend.controller;

import com.rahulshop.backend.entity.CartItem;
import com.rahulshop.backend.entity.User;
import com.rahulshop.backend.service.CartItemService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    // Get logged-in user's cart
    @GetMapping
    public List<CartItem> getCartItems(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return cartItemService.getCartItemsByUser(user.getId());
    }

    // Add item to cart
    @PostMapping
    public CartItem addToCart(
            @RequestBody CartItem cartItem,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        cartItem.setUserId(user.getId());

        return cartItemService.addToCart(cartItem);
    }

    // Update quantity
    @PutMapping("/{id}")
    public CartItem updateQuantity(
            @PathVariable Long id,
            @RequestParam int quantity,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return cartItemService.updateQuantity(
                id,
                quantity,
                user.getId()
        );
    }

    // Clear logged-in user's cart
    @DeleteMapping
    public String clearCart(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        cartItemService.clearCart(user.getId());

        return "Cart cleared successfully";
    }

    // Remove item from logged-in user's cart
    @DeleteMapping("/{id}")
    public String removeFromCart(
            @PathVariable Long id,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        cartItemService.removeFromCart(
                id,
                user.getId()
        );

        return "Item removed from cart";
    }
}