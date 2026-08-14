package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.modal.Cart;
import com.swiftcart.ecommerce.modal.Coupon;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.repository.CartRepository;
import com.swiftcart.ecommerce.repository.CouponRepository;
import com.swiftcart.ecommerce.repository.UserRepository;
import com.swiftcart.ecommerce.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    @Override
    public Cart applyCoupone(String code, double orderValue, User user) throws Exception {
        Coupon coupon =  couponRepository.findByCode(code);
        Cart cart = cartRepository.findByUserId(user.getId());

        if(coupon == null) {
            throw new Exception("Coupon Not Found!!!");
        }
        if(user.getUsedCoupons().contains(coupon)) {
            throw new Exception("Coupon Already Used!!!");
        }
        if(orderValue<coupon.getMinimumOrderValue()) {
            throw new Exception("Order Value Not Enough!!!" + coupon.getMinimumOrderValue());
        }
        if(coupon.isActive() && LocalDate.now().isAfter(coupon.getValidityStartDate()) && LocalDate.now().isBefore(coupon.getValidityEndDate())) {
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;
            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice);
            cart.setCouponCode(coupon.getCode());
            cartRepository.save(cart);
            return cart;
        }
        throw new Exception("Coupon Not Valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        Coupon coupon =  couponRepository.findByCode(code);
        if(coupon == null) {
            throw new Exception("Coupon Not Found...");
        }
        Cart cart = cartRepository.findByUserId(user.getId());
        double DiscountPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + DiscountPrice);
        cart.setCouponCode(null);
        return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long couponId) throws Exception {
        return couponRepository.findById(couponId).orElseThrow(() -> new Exception("Coupon Not Exist"));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupon() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCoupon(Long couponId) throws Exception {
        Coupon coupon = findCouponById(couponId);
        if(coupon == null) {
            throw new Exception("Coupon Not Found...");
        }
        couponRepository.delete(coupon);
    }
}
