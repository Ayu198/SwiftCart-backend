package com.swiftcart.ecommerce.repository;

import com.swiftcart.ecommerce.modal.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}
