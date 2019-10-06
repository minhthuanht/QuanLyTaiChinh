package com.minhthuanht.quanlytaichinh.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.common.util.IOUtils;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.common.Constants;

import java.io.InputStream;


public class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "DB_HELPER";
    private static final int DB_VERSION = 2;

    private Context mContext;

    public DBHelper(Context context) {
        super(context, Constants.DEFAULT_DB_FILENAME, null, DB_VERSION);
        if (context != null) {
            mContext = context;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
        onUpgrade(db, 1, DB_VERSION);
    }

    private void createDatabase(SQLiteDatabase db) {
        executeRawSql(db, R.raw.financial_depository_database);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //cập nhật cơ sở dữ liệu
        for (int i = oldVersion + 1; i <= newVersion; ++i) {
            // lấy id của tập tin update trong thư mục raw
            int identifier = mContext.getResources().getIdentifier(
                    String.format("update_database_v%d", i),
                    "raw",
                    mContext.getPackageName());
            if (identifier != 0) executeRawSql(db, identifier);
        }
    }

    private void executeRawSql(SQLiteDatabase db, int identifier) {
        InputStream inputStream = mContext.getResources().openRawResource(identifier);

        String sqlRaw = "";
        try {

            sqlRaw = new String(IOUtils.toByteArray(inputStream));
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }

        String sqlStatement[] = sqlRaw.split(";");

        //process all statement

        for (String aSqlStatement : sqlStatement) {
            Log.d(LOG_TAG, aSqlStatement);

            try {

                db.execSQL(aSqlStatement);
            } catch (Exception ex) {
                String errorMess = ex.getMessage();
                if (ex instanceof SQLiteException && errorMess != null && errorMess.contains("not an error (code 0)")) {
                    Log.v(LOG_TAG, errorMess);
                } else {
                    Log.e(LOG_TAG, "executing raw sql: " + aSqlStatement);
                }
            }
        }
    }
}
