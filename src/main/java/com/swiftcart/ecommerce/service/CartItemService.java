package com.swiftcart.ecommerce.service;

import com.swiftcart.ecommerce.modal.CartItem;

public interface CartItemService {
    CartItem updateCartItem(Long userId , Long id , CartItem cartItem) throws Exception;
    void deleteCartItem(Long userId , Long cartItemId) throws Exception;
    CartItem findCartItemById(Long userId) throws Exception;
}
