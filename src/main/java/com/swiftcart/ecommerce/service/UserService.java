package com.swiftcart.ecommerce.service;

import com.swiftcart.ecommerce.modal.User;

public interface UserService {
    User findByJwtToken(String jwt) throws Exception;
    User findByEmail(String email) throws Exception;
}
