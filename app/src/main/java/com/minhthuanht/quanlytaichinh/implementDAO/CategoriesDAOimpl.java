package com.minhthuanht.quanlytaichinh.implementDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;

import com.minhthuanht.quanlytaichinh.database.DBHelper;
import com.minhthuanht.quanlytaichinh.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoriesDAOimpl implements ICategoriesDAO {

    private static final String TABLE_CATEGORY_NAME = "tbl_categories";
    private static final String COLUMN_CATEGORY_ID = "_id";
    private static final String COLUMN_CATEGORY_TYPE = "type";
    private static final String COLUMN_CATEGORY_CATEGORY = "category";
    private static final String COLUMN_CATEGORY_ICON = "icon";

    private DBHelper mDBHelper;

    public CategoriesDAOimpl(Context context) {
        if (context != null) {
            mDBHelper = new DBHelper(context);
        }
    }


    @Override
    public boolean insertCategory(Category category) {

        if (category == null) return false;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CATEGORY_TYPE, category.getCategoryType().getValue());
        cv.put(COLUMN_CATEGORY_CATEGORY, category.getCategory());
        cv.put(COLUMN_CATEGORY_ICON, category.getCategoryIcon());

        int id = (int) db.insert(TABLE_CATEGORY_NAME, null, cv);
        db.close();
        return id != -1;
    }

    @Override
    public boolean updateCategory(Category category) {

        if (category == null) return false;

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CATEGORY_TYPE, category.getCategoryType().getValue());
        cv.put(COLUMN_CATEGORY_CATEGORY, category.getCategory());
        cv.put(COLUMN_CATEGORY_ICON, category.getCategoryIcon());

        db.update(TABLE_CATEGORY_NAME, cv, COLUMN_CATEGORY_ID + " =? ",
                new String[]{String.valueOf(category.getCategoryID())});
        db.close();

        return true;
    }

    @Override
    public boolean deleteCategory(Category category) {

        if (category == null) return false;

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        db.delete(TABLE_CATEGORY_NAME, COLUMN_CATEGORY_ID + " =? ",
                new String[]{String.valueOf(category.getCategoryID())});
        db.close();
        return true;
    }

    @Override
    public List<Category> getAllCategory() {

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY_NAME;
        Cursor cursor = db.rawQuery(query, null);

        List<Category> categories = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                Category category = new Category();
                category.setCategoryID(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
                category.setCategoryType(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_TYPE)));
                category.getCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_CATEGORY)));
                category.setCategoryIcon(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ICON)));

                categories.add(category);
            }

            cursor.close();
            db.close();

            return categories;
        }

        return null;
    }

    @Override
    public List<Category> getCategoriesByType(int type) {

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY_NAME + " WHERE " + COLUMN_CATEGORY_TYPE + " =? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(type)});

        List<Category> categories = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                Category category = new Category();
                category.setCategoryID(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
                category.setCategoryType(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_TYPE)));
                category.getCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_CATEGORY)));
                category.setCategoryIcon(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ICON)));

                categories.add(category);
            }

            cursor.close();
            db.close();

            return categories;
        }

        return null;
    }


    @Override
    public Category getCategoryById(int typeId) {

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY_NAME + " WHERE " + COLUMN_CATEGORY_ID + " =? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(typeId)});

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            Category category = new Category();
            category.setCategoryID(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
            category.setCategoryType(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_TYPE)));
            category.getCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_CATEGORY)));
            category.setCategoryIcon(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ICON)));

            cursor.close();
            db.close();

            return category;
        }
        return null;
    }

}
