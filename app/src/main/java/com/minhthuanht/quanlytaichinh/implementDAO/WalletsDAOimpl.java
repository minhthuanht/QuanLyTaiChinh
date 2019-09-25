package com.minhthuanht.quanlytaichinh.implementDAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.minhthuanht.quanlytaichinh.database.DBHelper;
import com.minhthuanht.quanlytaichinh.model.Wallet;

import java.util.ArrayList;
import java.util.List;

public class WalletsDAOimpl implements IWalletsDAO {

    private static final String LOG_TAG = "WallettDAOImpl";

    private static final String TABLE_WALLET_NAME = "tbl_wallets";
    private static final String COLUMN_WALLET_ID = "_id";
    private static final String COLUMN_WALLET_NAME = "name";
    private static final String COLUMN_WALLET_BALANCE = "balance";
    private static final String COLUMN_WALLET_CURRENCY = "currency";
    private static final String COLUMN_WALLET_USERID = "userId";

    private DBHelper mDbHelper;

    public WalletsDAOimpl(Context context) {
        if (context != null) {
            mDbHelper = new DBHelper(context);
        }
    }

    @Override
    public boolean insertWallet(Wallet wallet) {

        if (wallet == null) return false;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_WALLET_NAME, wallet.getWalletName());
        contentValues.put(COLUMN_WALLET_BALANCE, wallet.getWalletBalance());
        contentValues.put(COLUMN_WALLET_CURRENCY, wallet.getWalletCurrency());
        contentValues.put(COLUMN_WALLET_USERID, wallet.getWalletUserID());

        int id = (int) db.insert(TABLE_WALLET_NAME, null, contentValues);
        wallet.setWalletID(id);

        return id != -1;
    }

    @Override
    public boolean updateWallet(Wallet wallet) {

        if (wallet == null) return false;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_WALLET_NAME, wallet.getWalletName());
        contentValues.put(COLUMN_WALLET_BALANCE, wallet.getWalletBalance());
        contentValues.put(COLUMN_WALLET_CURRENCY, wallet.getWalletCurrency());
        contentValues.put(COLUMN_WALLET_USERID, wallet.getWalletUserID());

        db.update(TABLE_WALLET_NAME, contentValues, COLUMN_WALLET_ID + " =? ", new String[]{String.valueOf(wallet.getWalletID())});

        return true;
    }

    @Override
    public boolean deleteWallet(long walletId) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(TABLE_WALLET_NAME, COLUMN_WALLET_ID + " =? ", new String[]{String.valueOf(walletId)});


        return true;
    }

    @Override
    public Wallet getWalletById(int id) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_WALLET_NAME + " WHERE " + COLUMN_WALLET_ID + " =? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();
            return getWalletFromData(cursor);
        }
        return null;
    }


    @Override
    public boolean hasWallet(String userId) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_WALLET_NAME + " WHERE " + COLUMN_WALLET_USERID + " =? ";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        return cursor != null && cursor.getCount() > 0;
    }

    @Override
    public List<Wallet> getAllWalletByUser(String userId) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_WALLET_NAME + " WHERE " + COLUMN_WALLET_USERID + " =? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        List<Wallet> wallets = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                Wallet wallet = getWalletFromData(cursor);
                wallets.add(wallet);
            }

            cursor.close();


            return wallets;
        }

        return null;
    }

    @Override
    public long getIDMax() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_WALLET_NAME + " ORDER BY " + COLUMN_WALLET_ID + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);

        Wallet wallet = null;

        if (cursor != null && cursor.getCount() > 0) {

            wallet = getWalletFromData(cursor);
        }

        assert wallet != null;
        return wallet.getWalletID();
    }

    private Wallet getWalletFromData(Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_WALLET_ID));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_WALLET_NAME));
        float balance = cursor.getFloat(cursor.getColumnIndex(COLUMN_WALLET_BALANCE));
        String currency = cursor.getString(cursor.getColumnIndex(COLUMN_WALLET_CURRENCY));
        String userId = cursor.getString(cursor.getColumnIndex(COLUMN_WALLET_USERID));

        return new Wallet(id, name, balance, currency, userId);
    }

}
