package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.modal.Product;
import com.swiftcart.ecommerce.modal.Review;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.repository.ReviewProductRepository;
import com.swiftcart.ecommerce.response.CreateReviewRequest;
import com.swiftcart.ecommerce.service.ReviewProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewProductServiceImpl implements ReviewProductService {

    private final ReviewProductRepository reviewProductRepository;

    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {
        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());

        product.getReviews().add(review);
        return reviewProductRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewProductRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, double reviewRating, String reviewText, Long userId) throws Exception {
        Review review = findReviewById(reviewId);
        if(review.getUser().getId().equals(userId)){
            review.setRating(reviewRating);
            review.setReviewText(reviewText);
            reviewProductRepository.save(review);
        }
        throw new Exception("You can't update this Review");
    }

    @Override
    public Void deleteReview(Long reviewId, Long userId) throws Exception {
        Review review = findReviewById(reviewId);
        if(!review.getUser().getId().equals(userId)){
            throw new Exception("You can't delete this Review");
        }
        reviewProductRepository.delete(review);
        return null;
    }

    @Override
    public Review findReviewById(Long reviewId) throws Exception {
        return reviewProductRepository.findById(reviewId)
                .orElseThrow(() -> new Exception("Review Not found"));
    }
}
