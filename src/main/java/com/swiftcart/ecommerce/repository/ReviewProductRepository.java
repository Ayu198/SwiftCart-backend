package com.swiftcart.ecommerce.repository;

import com.swiftcart.ecommerce.modal.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewProductRepository extends JpaRepository<Review , Long> {
    List<Review> findByProductId(Long productId);
}
