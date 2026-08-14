package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.config.JwtProvider;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.repository.UserRepository;
import com.swiftcart.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    @Override
    public User findByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.findByEmail(email);
    }

    @Override
    public User findByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new Exception("user not found!!!");
        }
        return user;
    }
}
