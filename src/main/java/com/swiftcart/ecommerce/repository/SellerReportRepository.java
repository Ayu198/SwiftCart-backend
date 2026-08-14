package com.swiftcart.ecommerce.repository;

import com.swiftcart.ecommerce.modal.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerReportRepository extends JpaRepository<SellerReport,Long> {
    SellerReport findBySellerId(Long SellerId);
}
