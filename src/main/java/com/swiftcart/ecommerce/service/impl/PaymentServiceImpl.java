package com.swiftcart.ecommerce.service.impl;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.swiftcart.ecommerce.domain.PaymentOrderStatus;
import com.swiftcart.ecommerce.domain.PaymentStatus;
import com.swiftcart.ecommerce.modal.Order;
import com.swiftcart.ecommerce.modal.PaymentOrder;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.repository.OrderRepository;
import com.swiftcart.ecommerce.repository.PaymentOrderRepository;
import com.swiftcart.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;

    @Value("${RAZORPAY_KEY_ID}")
    private String apiKey;
    @Value("${RAZORPAY_KEY_SECRET}")
    private String apiSecret;
    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);
        return  paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
        return paymentOrderRepository.findById(orderId).orElseThrow(
                () -> new Exception("Payment Not Found"));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(paymentId);
        if(paymentOrder == null) {
            throw new Exception("Payment Order not Found");
        }
        return paymentOrder;
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder,
                                       String paymentId,
                                       String paymentLinkId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            RazorpayClient razorpay = new RazorpayClient(apiKey , apiSecret);
            Payment payment = razorpay.payments.fetch(paymentId);

            String status = payment.get("status");
            if(status.equals("captured")) {
                Set<Order> orders =  paymentOrder.getOrders();
                for(Order order : orders) {
                    order.setPaymentStatus(PaymentStatus.COMPLETED);
                    orderRepository.save(order);
                }
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            }
        }
        paymentOrder.setStatus(PaymentOrderStatus.FAIL);
        paymentOrderRepository.save(paymentOrder);
        return false;
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {
        amount = amount * 100;

        try {
            RazorpayClient razorpay = new RazorpayClient(apiKey , apiSecret);
            JSONObject paymentLinkRequest = getJsonObject(user, amount, orderId);

            PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);
            String paymentLinkUrl = paymentLink.get("short_url");
            String paymentLinkId = paymentLink.get("id");
            return paymentLink;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RazorpayException(e.getMessage());
        }
    }

    private static JSONObject getJsonObject(User user, Long amount, Long orderId) {
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", amount);
        paymentLinkRequest.put("currency","INR");

        JSONObject customer = new JSONObject();
        customer.put("name" , user.getFullName());
        customer.put("email" , user.getEmail());
        paymentLinkRequest.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email" , true);
        paymentLinkRequest.put("notify", notify);

        paymentLinkRequest.put("callback_url" , "http://localhost:5173/payment-success/" + orderId);
        paymentLinkRequest.put("callback_method" ,"get");
        return paymentLinkRequest;
    }

    @Override
    public String createStripePaymentLink(User user , Long amount , Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/"+orderId)
                .setCancelUrl("http://localhost:3000/payment-cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount*100)
                                .setProductData(
                                        SessionCreateParams
                                                .LineItem.PriceData.ProductData
                                                .builder().setName("SwiftCart Payment")
                                                .build()
                                ).build()
                        ).build()
                ).build();
        Session session = Session.create(params);
        return session.getUrl();
    }
}
