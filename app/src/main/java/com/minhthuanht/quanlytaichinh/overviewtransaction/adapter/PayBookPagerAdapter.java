package com.minhthuanht.quanlytaichinh.overviewtransaction.adapter;

import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.overviewtransaction.fragment.FragmentPagerPB;

import java.util.ArrayList;
import java.util.List;

public class PayBookPagerAdapter extends FragmentStatePagerAdapter {


    private List<Transaction> mItems = new ArrayList<>();

    private String[] mTitles = {"CẦN TRẢ", "CẦN THU"};

    public PayBookPagerAdapter(FragmentManager fm, List<Transaction> listTransaction) {
        super(fm);

        if (listTransaction != null) {

            mItems.addAll(listTransaction);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Fragment getItem(int position) {

        List<Transaction> itemsDebt = new ArrayList<>();
        List<Transaction> itemsLoan = new ArrayList<>();

        for (Transaction tran : mItems) {

            if (tran.getTransactionCategoryID().getCategoryID() == 59) {

                itemsDebt.add(tran);

            }
            if (tran.getTransactionCategoryID().getCategoryID() == 57) {

                itemsLoan.add(tran);

            }
        }

        switch (position) {

            case 0:
                return FragmentPagerPB.newInstance(itemsDebt, 59);

            case 1:
                return FragmentPagerPB.newInstance(itemsLoan, 57);
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return mTitles[position];
    }
}
