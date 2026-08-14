package com.swiftcart.ecommerce.repository;

import com.swiftcart.ecommerce.modal.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<WishList , Long> {
    WishList findByUserId(Long userId);
}
