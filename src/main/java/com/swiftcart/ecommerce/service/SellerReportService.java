package com.swiftcart.ecommerce.service;

import com.swiftcart.ecommerce.modal.Seller;
import com.swiftcart.ecommerce.modal.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller sellerId);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
