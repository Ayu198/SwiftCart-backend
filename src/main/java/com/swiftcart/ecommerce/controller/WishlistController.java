package com.swiftcart.ecommerce.controller;

import com.swiftcart.ecommerce.modal.Product;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.modal.WishList;
import com.swiftcart.ecommerce.repository.WishlistRepository;
import com.swiftcart.ecommerce.service.ProductService;
import com.swiftcart.ecommerce.service.UserService;
import com.swiftcart.ecommerce.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistRepository wishlistRepository;
    private final WishlistService wishlistService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<WishList> getWishlistByUserId(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findByJwtToken(jwt);
        WishList wishList = wishlistService.getWishlistByUserId(user);
        return ResponseEntity.ok(wishList);
    }

    @PutMapping("/add-product/{productId}")
    public ResponseEntity<WishList> addProductToWishlist(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Product product = productService.findProductById(productId);
        User user = userService.findByJwtToken(jwt);
        WishList wishList = wishlistService.addProductToWishlist(user , product);
        return  ResponseEntity.ok(wishList);
    }
}
