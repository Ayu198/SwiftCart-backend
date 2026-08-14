package com.swiftcart.ecommerce.service;

import com.swiftcart.ecommerce.modal.Order;
import com.swiftcart.ecommerce.modal.Seller;
import com.swiftcart.ecommerce.modal.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySellerId(Seller seller);
    List<Transaction> getAllTransaction();
}
