package com.swiftcart.ecommerce.controller;

import com.swiftcart.ecommerce.domain.OrderStatus;
import com.swiftcart.ecommerce.exception.SellerException;
import com.swiftcart.ecommerce.modal.Order;
import com.swiftcart.ecommerce.modal.Seller;
import com.swiftcart.ecommerce.service.OrderService;
import com.swiftcart.ecommerce.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    private final OrderService orderService;
    private final SellerService sellerService;

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersHandler(
            @RequestHeader("Authorization") String jwt
    ) throws SellerException {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Order> orders = orderService.sellersOrderHistory(seller.getId());
        return new ResponseEntity<>(orders , HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatusHandler(
            @PathVariable Long orderId,
            @PathVariable OrderStatus orderStatus,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        Order order = orderService.updateOrderStatus(orderId , orderStatus);
        return  new ResponseEntity<>(order , HttpStatus.ACCEPTED);
    }
}
