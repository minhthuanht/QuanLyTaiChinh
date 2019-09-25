package com.minhthuanht.quanlytaichinh.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Wallet implements Parcelable {

    private int walletID;
    private String walletName;
    private Float walletBalance;
    private String walletCurrency;
    private String walletUserID;

    public Wallet() {
    }

    public Wallet(int walletID, String walletName, Float walletBalance, String walletCurrency, String walletUserID) {

        this.walletID = walletID;
        this.walletName = walletName;
        this.walletBalance = walletBalance;
        this.walletCurrency = walletCurrency;
        this.walletUserID = walletUserID;
    }

    public Wallet(String walletName, Float walletBalance, String walletCurrency, String walletUserID) {
        this.walletName = walletName;
        this.walletBalance = walletBalance;
        this.walletCurrency = walletCurrency;
        this.walletUserID = walletUserID;
    }

    private Wallet(Parcel in) {
        walletID = in.readInt();
        walletName = in.readString();
        if (in.readByte() == 0) {
            walletBalance = null;
        } else {
            walletBalance = in.readFloat();
        }
        walletCurrency = in.readString();
        walletUserID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(walletID);
        dest.writeString(walletName);
        if (walletBalance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(walletBalance);
        }
        dest.writeString(walletCurrency);
        dest.writeString(walletUserID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };

    public int getWalletID() {
        return walletID;
    }

    public void setWalletID(int walletID) {
        this.walletID = walletID;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public Float getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Float walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getWalletCurrency() {
        return walletCurrency;
    }

    public void setWalletCurrency(String walletCurrency) {
        this.walletCurrency = walletCurrency;
    }

    public String getWalletUserID() {
        return walletUserID;
    }

    public void setWalletUserID(String walletUserID) {
        this.walletUserID = walletUserID;
    }

}
