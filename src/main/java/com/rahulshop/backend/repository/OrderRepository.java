package com.rahulshop.backend.repository;

import com.rahulshop.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Logged-in user ke orders
    List<Order> findByUserId(Long userId);

    // Sirf us user ka specific order
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}