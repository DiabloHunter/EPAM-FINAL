package com.epam.project.dao;

import com.epam.project.domain.Transaction;
import com.epam.project.domain.TransactionType;
import com.epam.project.exceptions.DataNotFoundException;

import java.sql.Timestamp;
import java.util.List;

/**
 * CRUD operations interface for Transaction entity
 */
public interface ITransactionDao {


    /**
     * Finds all transactions in DB
     * @return List of all transactions
     * @throws DataNotFoundException if connection is down, broken or unable to retrieve information for certain reasons
     */
    List<Transaction> findAllTransactions() throws DataNotFoundException;


    /**
     * Finds all transactions in DB by transaction type (PAYMENT or REFUND)
     * @param type - transaction type (PAYMENT or REFUND)
     * @return List of transactions by transaction type
     * @throws DataNotFoundException if connection is down, broken or unable to retrieve information for certain reasons
     */
    List<Transaction> findAllTransactionsByType(TransactionType type) throws DataNotFoundException;

    /**
     * Finds the sum of transactions price by date
     * @param timestamp - transaction date
     * @return String of amount of money
     * @throws DataNotFoundException if connection is down, broken or unable to retrieve information for certain reasons
     */
    String findAllMoneyByType(String timestamp) throws DataNotFoundException;


    /**
     * Adds new transaction in DB
     * @param transaction - transaction to add in DB
     * @return true if operation success and false if fails
     */
    boolean addTransactionToDB(Transaction transaction);
}