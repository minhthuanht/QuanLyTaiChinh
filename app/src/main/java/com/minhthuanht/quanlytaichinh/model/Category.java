package com.minhthuanht.quanlytaichinh.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

    private int categoryID;
    private TransactionTypes categoryType;
    private String category;
    private String categoryIcon;

    public Category() {
    }

    public Category(int categoryID, int categoryType, String category, String categoryIcon) {
        this.categoryID = categoryID;
        this.categoryType = TransactionTypes.from(categoryType);
        this.category = category;
        this.categoryIcon = categoryIcon;
    }

    private Category(Parcel in) {
        categoryID = in.readInt();
        categoryType = TransactionTypes.values()[in.readInt()];
        category = in.readString();
        categoryIcon = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoryID);
        dest.writeInt(categoryType.ordinal());
        dest.writeString(category);
        dest.writeString(categoryIcon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public TransactionTypes getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = TransactionTypes.from(categoryType);
    }

    public String getCategory() {
        return category;
    }

    public void getCategory(String category) {
        this.category = category;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

}
