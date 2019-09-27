package com.minhthuanht.quanlytaichinh.transaction.adapter;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TransactionPagerAdapter extends FragmentStatePagerAdapter {

    private List<Pair<String, Fragment>> mListFragment = new ArrayList<>();

    public TransactionPagerAdapter(FragmentManager fm, List<Pair<String, Fragment>> tabFragment) {

        super(fm);
        if (tabFragment != null) {

            mListFragment.addAll(tabFragment);
        }
    }

    @Override
    public Fragment getItem(int position) {// tạo ra ra từng fragment viewpager
        return mListFragment.get(position).second;
    }

    // hien thi ra bao nhieu cai fragment
    @Override
    public int getCount() {
        return mListFragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mListFragment.get(position).first;
    }

    public void updateViewPager(List<Pair<String, Fragment>> listFragment) {

        mListFragment.clear();
        if (listFragment != null) {
            mListFragment.addAll(listFragment);
            notifyDataSetChanged();
        }
    }


    // reload pager

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
