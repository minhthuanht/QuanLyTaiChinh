package com.minhthuanht.quanlytaichinh.budget.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.minhthuanht.quanlytaichinh.budget.fragment.FragmentListBudget;
import com.minhthuanht.quanlytaichinh.model.Budget;
import com.minhthuanht.quanlytaichinh.model.MTDate;

import java.util.ArrayList;
import java.util.List;

public class BudgetPagerAdapter extends FragmentStatePagerAdapter {

    private List<Budget> mBudgets = new ArrayList<>();

    private String[] titles = {"ĐANG ÁP DỤNG", "ĐÃ KẾT THÚC"};

    public BudgetPagerAdapter(FragmentManager fm, List<Budget> budgetList) {
        super(fm);

        if (budgetList != null) {

            mBudgets.addAll(budgetList);
        }
    }

    @Override
    public Fragment getItem(int position) {

        List<Budget> budgetInTime = new ArrayList<>();
        List<Budget> budgetTimed = new ArrayList<>();

        MTDate now = new MTDate();

        for (Budget budget : mBudgets){

            if ((budget.getTimeEnd().getMillis() - now.getMillis()) >= 0){

                budgetInTime.add(budget);
            }

            else {
                budgetTimed.add(budget);
            }

        }

        switch (position) {

            case 0:
                return FragmentListBudget.newInstance(budgetInTime);

            case 1:
                return FragmentListBudget.newInstance(budgetTimed);
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
        return titles[position];
    }

    public void updateItems(List<Budget> budgetList) {

        if (budgetList != null){
            mBudgets.clear();
            mBudgets.addAll(budgetList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
