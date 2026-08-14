package com.swiftcart.ecommerce.repository;

import com.swiftcart.ecommerce.modal.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

}
