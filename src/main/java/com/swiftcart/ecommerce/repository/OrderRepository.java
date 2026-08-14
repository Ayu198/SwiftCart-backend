package com.swiftcart.ecommerce.repository;

import com.swiftcart.ecommerce.modal.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long id);
    List<Order> findBySellerId(Long sellerId);
}
