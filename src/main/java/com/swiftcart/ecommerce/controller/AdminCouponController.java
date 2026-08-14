package com.swiftcart.ecommerce.controller;

import com.swiftcart.ecommerce.modal.Cart;
import com.swiftcart.ecommerce.modal.Coupon;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.service.CartService;
import com.swiftcart.ecommerce.service.CouponService;
import com.swiftcart.ecommerce.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
public class AdminCouponController {
    private final CouponService couponService;
    private final UserService userService;
    private final CartService cartService;

    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(
            @RequestParam String apply,
            @RequestParam String code ,
            @RequestParam double orderValue,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findByJwtToken(jwt);
        Cart cart;
        if(apply.equals("true")) {
            cart = couponService.applyCoupone(code , orderValue , user);
        } else {
            cart = couponService.removeCoupon(code , user);
        }
        return  new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> createCoupone(
            @RequestBody Coupon coupon
    ) {
        Coupon createdCoupone = couponService.createCoupon(coupon);
        return ResponseEntity.ok(createdCoupone);
    }

    @DeleteMapping("admin/delete/{id}")
    public ResponseEntity<?> deleteCoupone(
            @PathVariable Long id
    ) throws Exception {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Coupon Deleted Successfully");
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = couponService.findAllCoupon();
        return  ResponseEntity.ok(coupons);
    }
}
