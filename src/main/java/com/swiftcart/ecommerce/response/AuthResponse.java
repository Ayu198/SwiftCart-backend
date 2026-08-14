package com.swiftcart.ecommerce.response;

import com.swiftcart.ecommerce.domain.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private USER_ROLE role;
    private String message;
}
