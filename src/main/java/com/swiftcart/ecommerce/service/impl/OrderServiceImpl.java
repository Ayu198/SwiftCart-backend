package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.domain.OrderStatus;
import com.swiftcart.ecommerce.domain.PaymentStatus;
import com.swiftcart.ecommerce.modal.*;
import com.swiftcart.ecommerce.repository.AddressRepository;
import com.swiftcart.ecommerce.repository.OrderItemRepository;
import com.swiftcart.ecommerce.repository.OrderRepository;
import com.swiftcart.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if(!user.getAddresses().contains(shippingAddress)){
            user.getAddresses().add(shippingAddress);
        }
        Address address = addressRepository.save(shippingAddress);
        Map<Long , List<CartItem>> itemsBySeller = cart.getCartItems().stream().collect(Collectors.
                groupingBy(item -> item.getProduct().getSeller().getId()));
        Set<Order> orders = new HashSet<>();
        for(Map.Entry<Long , List<CartItem>> entry : itemsBySeller.entrySet()){
            Long sellerId = entry.getKey();
            List<CartItem> cartItems = entry.getValue();

            int totalOrderPrice = cartItems.stream().mapToInt(CartItem::getSellingPrice).sum();
            int totalItem = cartItems.stream().mapToInt(CartItem::getQuantity).sum();

            Order newOrder = new Order();
            newOrder.setUser(user);
            newOrder.setSellerId(sellerId);
            newOrder.setTotalMrpPrice(totalOrderPrice);
            newOrder.setTotalSellingPrice(totalOrderPrice);
            newOrder.setTotalItem(totalItem);
            newOrder.setShippingAddress(address);
            newOrder.setOrderStatus(OrderStatus.PENDING);
            newOrder.setPaymentStatus(PaymentStatus.PENDING);

            Order savedOrder = orderRepository.save(newOrder);
            orders.add(savedOrder);

            List<OrderItem> orderItems = new ArrayList<>();

            for(CartItem cartItem : cartItems){
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(newOrder);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSellingPrice(cartItem.getSellingPrice());
                orderItem.setMrpPrice(cartItem.getMrpPrice());
                orderItem.setSize(cartItem.getSize());
                orderItem.setUserId(cartItem.getUserId());
                savedOrder.getOrderItem().add(orderItem);

                OrderItem savedItem = orderItemRepository.save(orderItem);
                orderItems.add(savedItem);
            }
        }
        return orders;
    }

    @Override
    public Order findOrderById(long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(()-> new Exception("Order not found ...."));
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrderHistory(Long sellersId) {
        return orderRepository.findBySellerId(sellersId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = findOrderById(orderId);
        if(!user.getId().equals(order.getUser().getId())) {
            throw new Exception("User is not owner of this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id).orElseThrow(()-> new Exception("Order Item does not Exist"));
    }
}
