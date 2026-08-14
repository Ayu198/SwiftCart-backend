package com.swiftcart.ecommerce.repository;

import com.swiftcart.ecommerce.modal.Cart;
import com.swiftcart.ecommerce.modal.CartItem;
import com.swiftcart.ecommerce.modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByCartAndProductAndSize(Cart cart , Product product , String size);
}
