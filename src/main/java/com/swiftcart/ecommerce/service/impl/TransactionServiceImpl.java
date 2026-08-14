package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.modal.Order;
import com.swiftcart.ecommerce.modal.Seller;
import com.swiftcart.ecommerce.modal.Transaction;
import com.swiftcart.ecommerce.repository.SellerRepository;
import com.swiftcart.ecommerce.repository.TransactionRepository;
import com.swiftcart.ecommerce.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    @Override
    public Transaction createTransaction(Order order) {
        Seller seller = sellerRepository.findById(order.getSellerId()).get();
        Transaction transaction = new Transaction();
        transaction.setSeller(seller);
        transaction.setOrder(order);
        transaction.setCustomer(order.getUser());
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionBySellerId(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }
}
