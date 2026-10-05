package com.rahulshop.backend.service;

import com.rahulshop.backend.entity.Order;
import com.rahulshop.backend.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Admin ke liye saare orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Logged-in user ke sirf apne orders
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // New order place
    public Order placeOrder(Order order) {
        return orderRepository.save(order);
    }

    // User ka specific order
    public Order getOrderByIdForUser(Long id, Long userId) {
        return orderRepository.findByIdAndUserId(id, userId)
                .orElse(null);
    }

    // Admin kisi bhi order ko ID se dekh sakta hai
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElse(null);
    }

    // Admin order status update karega
    public Order updateOrderStatus(Long id, String status) {

        Order order = orderRepository.findById(id)
                .orElse(null);

        if (order == null) {
            return null;
        }

        order.setStatus(status);

        return orderRepository.save(order);
    }
}