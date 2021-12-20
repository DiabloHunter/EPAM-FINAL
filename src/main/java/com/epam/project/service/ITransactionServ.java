package com.epam.project.service;

import com.epam.project.domain.Transaction;
import com.epam.project.domain.TransactionType;
import com.epam.project.exceptions.TransactionServiceException;

import java.sql.Timestamp;
import java.util.List;

public interface ITransactionServ {
    /**
     * Finds all transactions
     * @return List of transactions
     * @throws TransactionServiceException if unable to retrieve information for certain reasons
     */
    List<Transaction> findAllTransactions() throws TransactionServiceException;

    /**
     * Finds all transactions by transaction type (PAYMENT or REFUND)
     * @param type - transaction type
     * @return List of selected transactions
     * @throws TransactionServiceException if unable to retrieve information for certain reasons
     */
    List<Transaction> findAllTransactionsByType(TransactionType type) throws TransactionServiceException;

    /**
     * Finds money of all transactions till the date
     * @param timestamp - date and time
     * @return String of money
     * @throws TransactionServiceException if unable to retrieve information for certain reasons
     */
   String findAllTransactionsByTime(String timestamp) throws TransactionServiceException;
}
