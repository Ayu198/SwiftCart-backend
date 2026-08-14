package com.swiftcart.ecommerce.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.swiftcart.ecommerce.modal.Order;
import com.swiftcart.ecommerce.modal.PaymentOrder;
import com.swiftcart.ecommerce.modal.User;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createOrder(User user , Set<Order> orders);
    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;
    PaymentOrder getPaymentOrderByPaymentId(String paymentId) throws Exception;
    Boolean proceedPaymentOrder(PaymentOrder paymentOrder ,
                                String paymentId,
                                String paymentLinkId) throws RazorpayException;
    PaymentLink createRazorpayPaymentLink(User user , Long amount , Long orderId) throws RazorpayException;
    String createStripePaymentLink(User user , Long amount , Long orderId) throws RazorpayException, StripeException;
}
