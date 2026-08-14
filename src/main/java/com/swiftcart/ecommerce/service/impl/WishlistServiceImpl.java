package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.modal.Product;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.modal.WishList;
import com.swiftcart.ecommerce.repository.WishlistRepository;
import com.swiftcart.ecommerce.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public WishList createWishlist(User user) {
        WishList wishList =  new WishList();
        wishList.setUser(user);
        return wishlistRepository.save(wishList);
    }

    @Override
    public WishList getWishlistByUserId(User user) {
        WishList wishList =  wishlistRepository.findByUserId(user.getId());
        if(wishList == null){
            wishList = createWishlist(user);
        }
        return wishList;
    }

    @Override
    public WishList addProductToWishlist(User user, Product product) {
        WishList updateWishList = getWishlistByUserId(user);
        if(updateWishList.getProducts().contains(product)){
            updateWishList.getProducts().remove(product);
        } else {
            updateWishList.getProducts().add(product);
        }
        return wishlistRepository.save(updateWishList);
    }
}
