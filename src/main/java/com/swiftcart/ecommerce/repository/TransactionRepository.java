package com.swiftcart.ecommerce.repository;

import com.swiftcart.ecommerce.modal.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction , Long> {
    List<Transaction> findBySellerId(Long sellerId);
}
