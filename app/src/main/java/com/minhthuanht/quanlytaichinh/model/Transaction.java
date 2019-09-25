package com.minhthuanht.quanlytaichinh.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Transaction implements Parcelable {

    private int transactionId;
    private String transactionCurrency;
    private float transactionTrading;
    private Date transactionDate;
    private String transactionNote;
    private Category transactionCategoryID;
    private Wallet trasactionWalletID;

    public Transaction() {
    }

    public Transaction(int transactionId, String transactionCurrency, float transactionTrading, Date transactionDate, String transactionNote, Category transactionCategoryID, Wallet trasactionWalletID) {
        this.transactionId = transactionId;
        this.transactionCurrency = transactionCurrency;
        this.transactionTrading = transactionTrading;
        this.transactionDate = transactionDate;
        this.transactionNote = transactionNote;
        this.transactionCategoryID = transactionCategoryID;
        this.trasactionWalletID = trasactionWalletID;
    }

    public Transaction(String transactionCurrency, float transactionTrading, Date transactionDate, String transactionNote, Category transactionCategoryID, Wallet trasactionWalletID) {
        this.transactionCurrency = transactionCurrency;
        this.transactionTrading = transactionTrading;
        this.transactionDate = transactionDate;
        this.transactionNote = transactionNote;
        this.transactionCategoryID = transactionCategoryID;
        this.trasactionWalletID = trasactionWalletID;
    }

    private Transaction(Parcel in) {
        transactionId = in.readInt();
        transactionCurrency = in.readString();
        transactionTrading = in.readFloat();
        transactionNote = in.readString();
        transactionDate = (Date) in.readSerializable();
        transactionCategoryID = in.readParcelable(Category.class.getClassLoader());
        trasactionWalletID = in.readParcelable(Wallet.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(transactionId);
        dest.writeString(transactionCurrency);
        dest.writeFloat(transactionTrading);
        dest.writeString(transactionNote);
        dest.writeSerializable(transactionDate);
        dest.writeParcelable(transactionCategoryID, flags);
        dest.writeParcelable(trasactionWalletID, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public float getTransactionTrading() {
        return transactionTrading;
    }

    public void setTransactionTrading(float transactionTrading) {
        this.transactionTrading = transactionTrading;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionNote() {
        return transactionNote;
    }

    public void setTransactionNote(String transactionNote) {
        this.transactionNote = transactionNote;
    }

    public Category getTransactionCategoryID() {
        return transactionCategoryID;
    }

    public void setTransactionCategoryID(Category transactionCategoryID) {
        this.transactionCategoryID = transactionCategoryID;
    }

    public Wallet getTrasactionWalletID() {
        return trasactionWalletID;
    }

    public void setTrasactionWalletID(Wallet trasactionWalletID) {
        this.trasactionWalletID = trasactionWalletID;
    }

    public float getMoneyTradingWithSign() {
        int type = transactionCategoryID.getCategoryType().getValue();
        if (type == 1 || type == 3) {
            return -1 * Math.abs(transactionTrading);
        } else {
            return Math.abs(transactionTrading);
        }
    }

}
