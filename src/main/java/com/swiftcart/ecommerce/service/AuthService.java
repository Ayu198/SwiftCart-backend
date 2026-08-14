package com.swiftcart.ecommerce.service;

import com.swiftcart.ecommerce.domain.USER_ROLE;
import com.swiftcart.ecommerce.response.AuthResponse;
import com.swiftcart.ecommerce.response.LoginRequest;
import com.swiftcart.ecommerce.response.SignupRequest;

public interface AuthService {

    void sendLoginOtp(String email , USER_ROLE role) throws Exception;
    String createUser(SignupRequest req) throws Exception;
    AuthResponse signing(LoginRequest req);
}
