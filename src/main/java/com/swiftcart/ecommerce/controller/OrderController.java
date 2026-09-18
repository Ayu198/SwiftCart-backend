package com.swiftcart.ecommerce.controller;

import com.razorpay.PaymentLink;
import com.swiftcart.ecommerce.domain.PaymentMethod;
import com.swiftcart.ecommerce.modal.*;
import com.swiftcart.ecommerce.repository.OrderItemRepository;
import com.swiftcart.ecommerce.repository.PaymentOrderRepository;
import com.swiftcart.ecommerce.response.PaymentLinkResponse;
import com.swiftcart.ecommerce.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;

    @PostMapping()
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
            @RequestBody Address shippingAddress,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        User user = userService.findByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        Set<Order> orders = orderService.createOrder(user , shippingAddress , cart);
        PaymentOrder paymentOrder = paymentService.createOrder(user , orders);
        PaymentLinkResponse res = new PaymentLinkResponse();
        if(paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            PaymentLink paymentLink = paymentService.createRazorpayPaymentLink(
                    user ,
                    paymentOrder.getAmount() ,
                    paymentOrder.getId());
            String paymentUrl = paymentLink.get("short_url");
            String paymentUrlId = paymentLink.get("id");
            res.setPayment_link_url(paymentUrl);
            paymentOrder.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(paymentOrder);
        } else {
            String paymentUrl = paymentService.createStripePaymentLink(
                    user ,
                    paymentOrder.getAmount() ,
                    paymentOrder.getId());
            res.setPayment_link_url(paymentUrl);
        }
        return new ResponseEntity<>(res , HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistoryHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findByJwtToken(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findByJwtToken(jwt);
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(
            @PathVariable Long orderItemId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findByJwtToken(jwt);
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);
        return new ResponseEntity<>(orderItem, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findByJwtToken(jwt);
        Order order = orderService.cancelOrder(orderId , user);

        Seller seller = sellerService.getSellerById(order.getSellerId());
        SellerReport sellerReport = sellerReportService.getSellerReport(seller);

        sellerReport.setCancelOrders(sellerReport.getCancelOrders() + 1);
        sellerReport.setTotalRefund(sellerReport.getTotalRefund() + order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(sellerReport);

        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }
}