package com.swiftcart.ecommerce.service;

import com.swiftcart.ecommerce.domain.OrderStatus;
import com.swiftcart.ecommerce.modal.*;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<Order> createOrder(User user , Address shippingAddress , Cart cart);
    Order findOrderById(long orderId) throws Exception;
    List<Order> usersOrderHistory(Long userId);
    List<Order> sellersOrderHistory(Long sellersId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;
    Order cancelOrder(Long orderId , User user) throws Exception;
    OrderItem getOrderItemById(Long id) throws Exception;
}
