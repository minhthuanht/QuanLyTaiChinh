package com.minhthuanht.quanlytaichinh.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Budget implements Parcelable {
    private int budgetId;
    private Wallet wallet;
    private Category category;
    private float amount;
    private float spent;
    private MTDate timeStart;
    private MTDate timeEnd;
    private boolean isLoop;
    private String status;

    public Budget() {
    }

    public Budget(int budgetId, Wallet wallet, Category category, float amount, float spent, MTDate timeStart, MTDate timeEnd, boolean isLoop, String status) {
        this.budgetId = budgetId;
        this.wallet = wallet;
        this.category = category;
        this.amount = amount;
        this.spent = spent;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.isLoop = isLoop;
        this.status = status;
    }

    private Budget(Parcel in) {
        budgetId = in.readInt();
        wallet = in.readParcelable(Wallet.class.getClassLoader());
        category = in.readParcelable(Category.class.getClassLoader());
        amount = in.readFloat();
        spent = in.readFloat();
        timeStart = (MTDate) in.readSerializable();
        timeEnd = (MTDate) in.readSerializable();
        isLoop = in.readByte() != 0;
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(budgetId);
        dest.writeParcelable(wallet, flags);
        dest.writeParcelable(category, flags);
        dest.writeFloat(amount);
        dest.writeFloat(spent);
        dest.writeSerializable(timeStart);
        dest.writeSerializable(timeEnd);
        dest.writeByte((byte) (isLoop ? 1 : 0));
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Budget> CREATOR = new Creator<Budget>() {
        @Override
        public Budget createFromParcel(Parcel in) {
            return new Budget(in);
        }

        @Override
        public Budget[] newArray(int size) {
            return new Budget[size];
        }
    };

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getSpent() {
        return spent;
    }

    public void setSpent(float spent) {
        this.spent = spent;
    }

    public MTDate getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(MTDate timeStart) {
        this.timeStart = timeStart;
    }

    public MTDate getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(MTDate timeEnd) {
        this.timeEnd = timeEnd;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
