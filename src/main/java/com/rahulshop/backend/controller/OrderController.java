package com.rahulshop.backend.controller;

import com.rahulshop.backend.entity.Order;
import com.rahulshop.backend.entity.User;
import com.rahulshop.backend.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Get orders
    // USER -> sirf apne orders
    // ADMIN -> saare orders
    @GetMapping
    public List<Order> getOrders(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if ("ADMIN".equals(user.getRole())) {
            return orderService.getAllOrders();
        }

        return orderService.getOrdersByUser(user.getId());
    }

    // Place new order
    // userId automatically logged-in user ka hoga
    @PostMapping
    public Order placeOrder(
            @RequestBody Order order,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        order.setUserId(user.getId());

        return orderService.placeOrder(order);
    }

    // Get order by ID
    // USER -> sirf apna order
    // ADMIN -> koi bhi order
    @GetMapping("/{id}")
    public Order getOrderById(
            @PathVariable Long id,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if ("ADMIN".equals(user.getRole())) {
            return orderService.getOrderById(id);
        }

        return orderService.getOrderByIdForUser(
                id,
                user.getId()
        );
    }

    // Update order status
    // SecurityConfig ke according ADMIN hi kar sakta hai
    @PutMapping("/{id}/status")
    public Order updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return orderService.updateOrderStatus(id, status);
    }
}