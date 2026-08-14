package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.modal.Seller;
import com.swiftcart.ecommerce.modal.SellerReport;
import com.swiftcart.ecommerce.repository.SellerReportRepository;
import com.swiftcart.ecommerce.service.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerReportImpl implements SellerReportService {

    private final SellerReportService sellerReportService;
    private final SellerReportRepository sellerReportRepository;

    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport sellerReport = sellerReportRepository.findBySellerId(seller.getId());

        if(sellerReport == null) {
            SellerReport newSellerReport = new SellerReport();
            newSellerReport.setSeller(seller);
            return sellerReportRepository.save(newSellerReport);
        }
        return sellerReport;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }
}
