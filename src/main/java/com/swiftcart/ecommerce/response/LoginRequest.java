package com.swiftcart.ecommerce.response;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String otp;
}
