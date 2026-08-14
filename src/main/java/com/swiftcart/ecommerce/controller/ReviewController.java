package com.swiftcart.ecommerce.controller;

import com.swiftcart.ecommerce.modal.Product;
import com.swiftcart.ecommerce.modal.Review;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.response.ApiResponse;
import com.swiftcart.ecommerce.response.CreateReviewRequest;
import com.swiftcart.ecommerce.service.ProductService;
import com.swiftcart.ecommerce.service.ReviewProductService;
import com.swiftcart.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewProductService reviewProductService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/product/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(
            @PathVariable Long productId
    ) {
        List<Review> reviews = reviewProductService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/product/{produtId}/reviews")
    public ResponseEntity<Review> writeReview(
            @PathVariable Long produtId,
            @RequestBody CreateReviewRequest req,
            @RequestHeader("Authorizatoin") String jwt
    ) throws Exception {
        User user = userService.findByJwtToken(jwt);
        Product product = productService.findProductById(produtId);

        Review review = reviewProductService.createReview(req, user, product);
        return  ResponseEntity.ok(review);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @RequestBody CreateReviewRequest req,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findByJwtToken(jwt);

        Review review = reviewProductService.updateReview(
                reviewId,
                req.getReviewRating(),
                req.getReviewText(),
                user.getId()
        );
        return  ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findByJwtToken(jwt);
        reviewProductService.deleteReview(reviewId , user.getId());
        ApiResponse response = new ApiResponse();
        response.setMessage("Review deleted successfully");
        return ResponseEntity.ok(response);
    }
}
