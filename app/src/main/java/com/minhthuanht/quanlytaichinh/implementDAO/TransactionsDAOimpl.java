package com.minhthuanht.quanlytaichinh.implementDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.minhthuanht.quanlytaichinh.database.DBHelper;
import com.minhthuanht.quanlytaichinh.model.Category;
import com.minhthuanht.quanlytaichinh.model.DateRange;
import com.minhthuanht.quanlytaichinh.model.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionsDAOimpl implements ITransactionsDAO {

    private static final String TABLE_TRANSACTION_NAME = "tbl_transactions";
    private static final String COLUMN_TRANSACTION_ID = "_id";
    private static final String COLUMN_TRANSACTION_CURRENCY = "currency";
    private static final String COLUMN_TRANSACTION_TRADING = "trading";
    private static final String COLUMN_TRANSACTION_DATE = "transaction_date";
    private static final String COLUMN_TRANSACTION_NOTE = "note";
    private static final String COLUMN_TRANSACTION_CATEGORYID = "categoryId";
    private static final String COLUMN_TRANSACTION_WALLETID = "walletId";

    private DBHelper mDbHelper;

    private IWalletsDAO mIWalletsDAO;

    private ICategoriesDAO mICategoriesDAO;

    public TransactionsDAOimpl(Context context) {
        if (context != null) {
            mDbHelper = new DBHelper(context);
            mIWalletsDAO = new WalletsDAOimpl(context);
            mICategoriesDAO = new CategoriesDAOimpl(context);
        }
    }

    @Override
    public boolean insertTransaction(Transaction transaction) {

        if (transaction == null) return false;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TRANSACTION_CURRENCY, transaction.getTransactionCurrency());
        contentValues.put(COLUMN_TRANSACTION_TRADING, transaction.getTransactionTrading());
        contentValues.put(COLUMN_TRANSACTION_DATE, transaction.getTransactionDate().getTime());
        contentValues.put(COLUMN_TRANSACTION_NOTE, transaction.getTransactionNote());
        contentValues.put(COLUMN_TRANSACTION_CATEGORYID, transaction.getTransactionCategoryID().getCategoryID());
        contentValues.put(COLUMN_TRANSACTION_WALLETID, transaction.getTransactionWalletID().getWalletID());

        int id = (int) db.insert(TABLE_TRANSACTION_NAME, null, contentValues);
        db.close();

        return id != -1;
    }

    @Override
    public boolean updateTransaction(Transaction transaction) {

        if (transaction == null) return false;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TRANSACTION_CURRENCY, transaction.getTransactionCurrency());
        contentValues.put(COLUMN_TRANSACTION_TRADING, transaction.getTransactionTrading());
        contentValues.put(COLUMN_TRANSACTION_DATE, transaction.getTransactionDate().getTime());
        contentValues.put(COLUMN_TRANSACTION_NOTE, transaction.getTransactionNote());
        contentValues.put(COLUMN_TRANSACTION_CATEGORYID, transaction.getTransactionCategoryID().getCategoryID());
        contentValues.put(COLUMN_TRANSACTION_WALLETID, transaction.getTransactionWalletID().getWalletID());

        db.update(TABLE_TRANSACTION_NAME, contentValues, COLUMN_TRANSACTION_ID + " =? ", new String[]{String.valueOf(transaction
                .getTransactionId())});
        db.close();

        return true;
    }

    @Override
    public boolean deleteTransaction(Transaction transaction) {

        if (transaction == null) return false;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(TABLE_TRANSACTION_NAME, COLUMN_TRANSACTION_ID + " =? ", new String[]{String.valueOf(transaction.getTransactionId())});

        db.close();

        return true;
    }

    @Override
    public Transaction getTransactionById(long transactionId) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME + " WHERE " + COLUMN_TRANSACTION_ID + " =? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(transactionId)});

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();
            Transaction transaction = getTransactionByData(cursor);
            cursor.close();
            db.close();

            return transaction;
        }
        return null;
    }


    @Override
    public List<Transaction> getAllTransaction() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {

            List<Transaction> transactions = new ArrayList<>();

            while (cursor.moveToNext()) {

                Transaction transaction = getTransactionByData(cursor);
                transactions.add(transaction);
            }

            cursor.close();
            db.close();

            return transactions;
        }
        return null;
    }

    @Override
    public List<Transaction> getAllTransactionByWalletId(long walletId) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME + " WHERE " + COLUMN_TRANSACTION_WALLETID + " =? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(walletId)});

        if (cursor != null && cursor.getCount() > 0) {

            List<Transaction> transactions = new ArrayList<>();

            while (cursor.moveToNext()) {

                Transaction transaction = getTransactionByData(cursor);
                transactions.add(transaction);
            }

            cursor.close();
            db.close();

            return transactions;
        }
        return null;
    }

    @Override
    public List<Transaction> getStatisticalByCategoryInRange(long wallet_id ,int categoryId , DateRange dateRange) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "SELECT "
                + " tblt.trading AS trading "
                + " ,tblc._id AS categoryId "
                + " ,tblc.type AS type "
                + " ,tblt.transaction_date as t_date"
                + " FROM tbl_transactions tblt "
                + " INNER JOIN tbl_categories tblc ON tblc._id = tblt.categoryId "
                + " WHERE (tblt.categoryId = " + categoryId + " OR tblc.parentId = " + categoryId +" )"
                + " AND tblt.walletId = "+ wallet_id
                + " AND tblt.transaction_date >= " + dateRange.getDateFrom().getMillis()
                + " AND tblt.transaction_date <= " + dateRange.getDateTo().getMillis();
        List<Transaction> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,null);

        while(cursor.moveToNext()) {
            Category cat = new Category();
            float column1 = cursor.getFloat(cursor.getColumnIndex("trading"));
            int column2 = cursor.getInt(cursor.getColumnIndex("categoryId"));
            int column3 = cursor.getInt(cursor.getColumnIndex("type"));
            long column4 = cursor.getLong(cursor.getColumnIndex("t_date"));
            Transaction data = new Transaction();
            data.setTransactionTrading(column1);
            cat.setCategoryID(column2);
            cat.setCategoryType(column3);
            data.setTransactionCategoryID(cat);
            data.setTransactionDate(new Date(column4));
            list.add(data);
        }
        return list;
    }

    @Override
    public long getIDMax() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME + " ORDER BY " + COLUMN_TRANSACTION_ID + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);

        Transaction transaction = null;

        if (cursor != null && cursor.getCount() > 0) {

            transaction = getTransactionByData(cursor);
        }

        assert transaction != null;
        return transaction.getTransactionId();
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
