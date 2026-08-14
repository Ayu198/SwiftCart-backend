package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.modal.CartItem;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.repository.CartItemRepository;
import com.swiftcart.ecommerce.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {
        CartItem item = findCartItemById(id);
        User cartItemUser = item.getCart().getUser();
        if(cartItemUser.getId().equals(userId)){
            item.setQuantity(cartItem.getQuantity());
            item.setSellingPrice(item.getQuantity()*item.getProduct().getSellingPrice());
            item.setMrpPrice(item.getQuantity()*item.getProduct().getMrpPrice());
            return cartItemRepository.save(item);
        }
        throw new Exception("You can't update this cart items");
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) throws Exception {
        CartItem item =  findCartItemById(cartItemId);
        User cartItemUser = item.getCart().getUser();
        if(cartItemUser.getId().equals(userId)){
            cartItemRepository.delete(item);
        }
        else throw new Exception("You can't delete this cart");
    }

    @Override
    public CartItem findCartItemById(Long userId) throws Exception {
        return cartItemRepository.findById(userId).orElseThrow(() -> new Exception("Can't find the cart"));
    }
}
