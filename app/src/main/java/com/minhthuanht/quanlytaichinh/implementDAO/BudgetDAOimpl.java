package com.minhthuanht.quanlytaichinh.implementDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.minhthuanht.quanlytaichinh.database.DBHelper;
import com.minhthuanht.quanlytaichinh.model.Budget;
import com.minhthuanht.quanlytaichinh.model.Category;
import com.minhthuanht.quanlytaichinh.model.DateRange;
import com.minhthuanht.quanlytaichinh.model.MTDate;
import com.minhthuanht.quanlytaichinh.model.Wallet;

import java.util.ArrayList;
import java.util.List;

public class BudgetDAOimpl implements IBudgetDAO {

    private static final String TABLE_BUDGET = "tbl_budgets";
    private static final String BUDGET_ID = "_id";
    private static final String CATEGORY_ID = "categoryId";
    private static final String WALLET_ID = "walletId";
    private static final String AMOUNT = "amount";
    private static final String SPENT = "spent";
    private static final String TIME_START = "timeStart";
    private static final String TIME_END = "timeEnd";
    private static final String LOOP = "loop";
    private static final String STATUS = "status";

    private DBHelper mDbHelper;
    private ICategoriesDAO mICategoriesDAO;
    private ITransactionsDAO mITransactionsDAO;
    private IWalletsDAO mIWalletsDAO;

    public BudgetDAOimpl(Context context) {

        if (context != null) {

            mDbHelper = new DBHelper(context);
            mICategoriesDAO = new CategoriesDAOimpl(context);
            mITransactionsDAO = new TransactionsDAOimpl(context);
            mIWalletsDAO = new WalletsDAOimpl(context);
        }
    }

    @Override
    public boolean insertBudget(Budget budget) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CATEGORY_ID, budget.getCategory().getCategoryID());
        contentValues.put(WALLET_ID, budget.getWallet().getWalletID());
        contentValues.put(AMOUNT, budget.getAmount());
        contentValues.put(SPENT, budget.getSpent());
        contentValues.put(TIME_START, budget.getTimeStart().toDate().getTime());
        contentValues.put(TIME_END, budget.getTimeEnd().toDate().getTime());
        contentValues.put(LOOP, budget.isLoop());
        contentValues.put(STATUS, budget.getStatus());

        int id = (int) db.insert(TABLE_BUDGET, null, contentValues);
        db.close();

        return id != -1;
    }

    @Override
    public boolean updateBudget(Budget budget) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CATEGORY_ID, budget.getCategory().getCategoryID());
        contentValues.put(WALLET_ID, budget.getWallet().getWalletID());
        contentValues.put(AMOUNT, budget.getAmount());
        contentValues.put(SPENT, budget.getSpent());
        contentValues.put(TIME_START, budget.getTimeStart().toDate().getTime());
        contentValues.put(TIME_END, budget.getTimeEnd().toDate().getTime());
        contentValues.put(LOOP, budget.isLoop());
        contentValues.put(STATUS, budget.getStatus());

        db.update(TABLE_BUDGET, contentValues, BUDGET_ID + " =? ", new String[]{String.valueOf(budget.getBudgetId())});
        db.close();
        return true;
    }

    @Override
    public boolean deleteBudget(Budget budget) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(TABLE_BUDGET, BUDGET_ID + " =? ", new String[]{String.valueOf(budget.getBudgetId())});
        db.close();
        return true;
    }

    @Override
    public List<Budget> getAllBudget() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BUDGET;
        Cursor cursor = db.rawQuery(query, null);

        List<Budget> budgetList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                budgetList.add(getItemBudget(cursor));
            }
        }

        assert cursor != null;
        cursor.close();
        db.close();
        return budgetList;
    }


    @Override
    public List<Budget> getAllBudget(long walletId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BUDGET + " WHERE " + WALLET_ID + " =? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(walletId)});
        List<Budget> budgetList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                budgetList.add(getItemBudget(cursor));
            }
        }

        return budgetList;
    }

    @Override
    public List<Budget> getBudgetByPeriod(long walletId, DateRange dateRange) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BUDGET + " WHERE " + WALLET_ID + " =? "
                + " AND " + TIME_START + " >= ? " + " AND " +
                TIME_END + " <= ? ";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(walletId),
                String.valueOf(dateRange.getDateFrom().toDate().getTime()),
                String.valueOf(dateRange.getDateTo().toDate().getTime())});

        List<Budget> budgetList = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0){

            while (cursor.moveToNext()){
                budgetList.add(getItemBudget(cursor));
            }
        }

        assert cursor != null;
        cursor.close();
        db.close();
        return budgetList;
    }

    @Override
    public List<Budget> getBudgetByCategory(long walletId, int categoryId) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BUDGET + " WHERE " + WALLET_ID + " =? " + " AND " + CATEGORY_ID + " =? ";
        Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(walletId), String.valueOf(categoryId)
        });

        List<Budget> budgetList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0){

            while (cursor.moveToNext()){

                budgetList.add(getItemBudget(cursor));
            }
        }

        assert cursor != null;
        cursor.close();
        db.close();
        return budgetList;
    }

    @Override
    public void updateBudgetSpent(Budget budget) {

    }

    private Budget getItemBudget(Cursor cursor) {

        Budget budget = new Budget();
        int id = cursor.getInt(cursor.getColumnIndex(BUDGET_ID));
        budget.setBudgetId(id);
        int walletId = cursor.getInt(cursor.getColumnIndex(WALLET_ID));
        budget.setWallet(mIWalletsDAO.getWalletById(walletId));
        int categoryId = cursor.getInt(cursor.getColumnIndex(CATEGORY_ID));
        budget.setCategory(mICategoriesDAO.getCategoryById(categoryId));
        budget.setAmount(cursor.getFloat(cursor.getColumnIndex(AMOUNT)));
        budget.setSpent(cursor.getFloat(cursor.getColumnIndex(SPENT)));
        budget.setTimeStart(new MTDate(cursor.getLong(cursor.getColumnIndex(TIME_START))));
        budget.setTimeEnd(new MTDate(cursor.getLong(cursor.getColumnIndex(TIME_END))));
        budget.setLoop(cursor.getInt(cursor.getColumnIndex(LOOP)) != 0);
        budget.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));

        return budget;
    }
}
