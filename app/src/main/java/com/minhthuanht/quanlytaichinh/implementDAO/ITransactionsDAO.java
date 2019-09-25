package com.minhthuanht.quanlytaichinh.implementDAO;

import com.minhthuanht.quanlytaichinh.model.Transaction;

import java.util.List;

public interface ITransactionsDAO {
    boolean insertTransaction(Transaction transaction);
    boolean updateTransaction(Transaction transaction);
    boolean deleteTransaction(Transaction transaction);

    Transaction getTransactionById(long transactionId);

    List<Transaction> getAllTransaction();
    List<Transaction> getAllTransactionByWalletId(long walletId);
    long getIDMax();
}
