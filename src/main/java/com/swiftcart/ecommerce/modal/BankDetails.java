package com.swiftcart.ecommerce.modal;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class BankDetails {
    private String accountNumber;
    private String accountHolderName;
    private String ifscCode;
}
