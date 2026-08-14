package com.swiftcart.ecommerce.service;

import com.swiftcart.ecommerce.modal.Product;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.modal.WishList;

public interface WishlistService {
    WishList createWishlist(User user);
    WishList getWishlistByUserId(User user);
    WishList addProductToWishlist(User user , Product product);
}
