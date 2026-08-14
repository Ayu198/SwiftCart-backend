package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.domain.USER_ROLE;
import com.swiftcart.ecommerce.modal.Seller;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.repository.SellerRepository;
import com.swiftcart.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private static final String SELLER_PREFIX = "seller_";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.startsWith(SELLER_PREFIX)){
            String actualUser =  username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(actualUser);
            if(seller != null) {
                return buildUserDetail(seller.getEmail() , seller.getPassword() , seller.getRole());
            }
        } else {
            User user =  userRepository.findByEmail(username);
            if(user != null) {
                return buildUserDetail(user.getEmail() , user.getPassword(), user.getRole());
            }
        }
        throw new UsernameNotFoundException("User or Seller not found with username: " + username);
    }

    private UserDetails buildUserDetail(String email, String password, USER_ROLE role) {
        if(role == null) role = USER_ROLE.ROLE_CUSTOMER;
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority(role.toString()));
        return new org.springframework.security.core.userdetails.User(email, password, grantedAuthorityList);
    }
}
