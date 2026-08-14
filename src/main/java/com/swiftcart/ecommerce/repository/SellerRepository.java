package com.swiftcart.ecommerce.repository;

import com.swiftcart.ecommerce.domain.AccountStatus;
import com.swiftcart.ecommerce.modal.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus accountStatus);
}
