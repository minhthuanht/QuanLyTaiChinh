package com.minhthuanht.quanlytaichinh.implementDAO;

import com.minhthuanht.quanlytaichinh.model.DateRange;
import com.minhthuanht.quanlytaichinh.model.MTDate;
import com.minhthuanht.quanlytaichinh.model.Transaction;

import java.util.List;

public interface ISearchTransaction {
    List<Transaction> searchTransaction(int idCategory);
    List<Transaction> searchTransactionByDate(DateRange dateRange);
}
