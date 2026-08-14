package com.swiftcart.ecommerce.service;

import com.swiftcart.ecommerce.modal.Product;
import com.swiftcart.ecommerce.modal.Review;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.response.CreateReviewRequest;

import java.util.List;

public interface ReviewProductService {
    Review createReview(CreateReviewRequest req ,
                        User user,
                        Product product);
    List<Review> getReviewByProductId(Long productId);
    Review updateReview(Long reviewId , double reviewRating , String reviewText , Long userId) throws Exception;
    Void deleteReview(Long reviewId, Long userId) throws Exception;
    Review findReviewById(Long reviewId) throws Exception;
}
