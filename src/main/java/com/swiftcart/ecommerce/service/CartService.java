package com.swiftcart.ecommerce.service;


import com.swiftcart.ecommerce.modal.Cart;
import com.swiftcart.ecommerce.modal.CartItem;
import com.swiftcart.ecommerce.modal.Product;
import com.swiftcart.ecommerce.modal.User;

public interface CartService {

    public CartItem addCartItem(
           User user,
           Product product,
           String size,
           int quantity
    );
    public Cart findUserCart(User user);
}
