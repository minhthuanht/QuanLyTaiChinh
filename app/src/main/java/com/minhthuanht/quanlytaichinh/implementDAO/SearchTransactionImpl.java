package com.minhthuanht.quanlytaichinh.implementDAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.minhthuanht.quanlytaichinh.database.DBHelper;
import com.minhthuanht.quanlytaichinh.model.DateRange;
import com.minhthuanht.quanlytaichinh.model.MTDate;
import com.minhthuanht.quanlytaichinh.model.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchTransactionImpl implements ISearchTransaction {

    private static final String TABLE_TRANSACTION_NAME = "tbl_transactions";
    private static final String TABLE_CATEGORY_NAME = "tbl_categories";
    private static final String COLUMN_CATEGORY_ID = "_id";
    private static final String COLUMN_CATEGORY_TYPE = "type";
    private static final String COLUMN_TRANSACTION_ID = "_id";
    private static final String COLUMN_TRANSACTION_CURRENCY = "currency";
    private static final String COLUMN_TRANSACTION_TRADING = "trading";
    private static final String COLUMN_TRANSACTION_DATE = "transaction_date";
    private static final String COLUMN_TRANSACTION_NOTE = "note";
    private static final String COLUMN_TRANSACTION_CATEGORYID = "categoryId";
    private static final String COLUMN_TRANSACTION_WALLETID = "walletId";

    private DBHelper mDbHelper;
    private ICategoriesDAO mICategoriesDAO;
    private IWalletsDAO mIWalletsDAO;

    public SearchTransactionImpl(Context context) {
        if (context != null) {
            mDbHelper = new DBHelper(context);
            mICategoriesDAO = new CategoriesDAOimpl(context);
            mIWalletsDAO = new WalletsDAOimpl(context);
        }
    }

    @Override
    public List<Transaction> searchTransaction(int idCategory) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME + " INNER JOIN " + TABLE_CATEGORY_NAME + " ON " + TABLE_TRANSACTION_NAME
                + "." + COLUMN_TRANSACTION_CATEGORYID + "=" + TABLE_CATEGORY_NAME + "." + COLUMN_CATEGORY_ID +
                " WHERE " + TABLE_CATEGORY_NAME + "." + COLUMN_CATEGORY_TYPE + "=?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idCategory)});
        List<Transaction> transactions = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                Transaction transaction = getTransactionByData(cursor);
                transactions.add(transaction);
            }
        }
        cursor.close();
        db.close();
        return transactions;
    }


    @Override
    public List<Transaction> searchTransactionByDate(DateRange dateRange) {
        MTDate dateStart = dateRange.getDateFrom();
        MTDate dateEnd = dateRange.getDateTo();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME + " WHERE " + COLUMN_TRANSACTION_DATE + " >=" + dateStart.getMillis()
                + " AND " + COLUMN_TRANSACTION_DATE + " <= " + dateEnd.getMillis();
        Log.d("startend1", dateStart.getMillis() + "");
        Log.d("startend2", dateEnd.getMillis() + "");
        Cursor cursor = db.rawQuery(query, null);
        List<Transaction> transactions = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Transaction transaction = getTransactionByData(cursor);
                transactions.add(transaction);
            }

        }
        return transactions;
    }

    private Transaction getTransactionByData(Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
        String currency = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSACTION_CURRENCY));
        float trading = cursor.getFloat(cursor.getColumnIndex(COLUMN_TRANSACTION_TRADING));
        Date transaction_date = new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_TRANSACTION_DATE)));
        String note = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSACTION_NOTE));
        int categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_CATEGORYID));
        int walletId = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_WALLETID));

        return new Transaction(id, currency, trading, transaction_date, note, mICategoriesDAO.getCategoryById(categoryId), mIWalletsDAO.getWalletById(walletId));
    }
}
