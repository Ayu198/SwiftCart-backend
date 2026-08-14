package com.swiftcart.ecommerce.modal;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class BusinessDetails {
    private String businessName;
    private String businessEmail;
    private String businessMobile;
    private String businessAddress;
    private String logo;
    private String banner;
}
