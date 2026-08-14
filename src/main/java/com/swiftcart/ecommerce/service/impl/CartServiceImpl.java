package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.modal.Cart;
import com.swiftcart.ecommerce.modal.CartItem;
import com.swiftcart.ecommerce.modal.Product;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.repository.CartItemRepository;
import com.swiftcart.ecommerce.repository.CartRepository;
import com.swiftcart.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = findUserCart(user);

        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart,product,size);
        if(isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setSize(size);
            cartItem.setUserId(user.getId());
            int totalPrice = quantity*product.getSellingPrice();
            cartItem.setSellingPrice(totalPrice);
            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);
            cartItem.setMrpPrice(quantity*product.getMrpPrice());
            return cartItemRepository.save(cartItem);
        }
        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());

        int totalPrice = 0;
        int totalItem = 0;
        int totalDiscountedPrice = 0;

        for(CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountedPrice+=cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();
        }
        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(CalculateDiscountPercentage(totalPrice , totalDiscountedPrice));
        return cart;
    }

    private int CalculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice <= 0) {
            return 0;
        }
        double discountPrice = mrpPrice - sellingPrice;
        double discountPercentage=(discountPrice/mrpPrice) * 100;
        return (int) discountPercentage;
    }
}
