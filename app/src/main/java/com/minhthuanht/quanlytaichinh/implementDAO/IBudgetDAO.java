package com.minhthuanht.quanlytaichinh.implementDAO;

import com.minhthuanht.quanlytaichinh.model.Budget;
import com.minhthuanht.quanlytaichinh.model.DateRange;

import java.util.List;

public interface IBudgetDAO {

    boolean insertBudget(Budget budget);

    boolean updateBudget(Budget budget);

    boolean deleteBudget(Budget budget);

    List<Budget> getAllBudget();

    List<Budget> getAllBudget(long walletId);

    List<Budget> getBudgetByPeriod(long walletId, DateRange dateRange);

    List<Budget> getBudgetByCategory(long walletId, int categoryId);

    void updateBudgetSpent(Budget budget);
}
