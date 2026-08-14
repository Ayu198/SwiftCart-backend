package com.swiftcart.ecommerce.service;

import com.swiftcart.ecommerce.modal.Cart;
import com.swiftcart.ecommerce.modal.Coupon;
import com.swiftcart.ecommerce.modal.User;

import java.util.List;

public interface CouponService {
    Cart applyCoupone(String code , double orderValue , User user) throws Exception;
    Cart removeCoupon(String code , User user) throws Exception;
    Coupon findCouponById(Long couponId) throws Exception;
    Coupon createCoupon(Coupon coupon);
    List<Coupon> findAllCoupon();
    void deleteCoupon(Long couponId) throws Exception;
}
