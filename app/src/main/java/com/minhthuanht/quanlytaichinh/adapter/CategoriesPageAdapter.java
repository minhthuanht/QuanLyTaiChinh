package com.minhthuanht.quanlytaichinh.adapter;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.minhthuanht.quanlytaichinh.category.fragment.FragmentListCategories;
import com.minhthuanht.quanlytaichinh.model.Category;

import java.util.ArrayList;

import java.util.List;

public class CategoriesPageAdapter extends FragmentStatePagerAdapter {

    public interface ICategoriesAdapter{

        void handleItemClicked( Category category);
    }

    private ICategoriesAdapter mICategoriesAdapter;

    private FragmentListCategories.IFragmentListCategories mCategoriesInterface = new FragmentListCategories.IFragmentListCategories() {
        @Override
        public void onItemClicked(Category category) {

            mICategoriesAdapter.handleItemClicked(category);
        }
    };

    private List<Category> mListCategories = new ArrayList<>();

    private String[] titles = {
            "Đi vay/Cho vay", "Khoản chi", "Khoản thu"
    };

    public CategoriesPageAdapter(FragmentManager fm, List<Category> listCategory, ICategoriesAdapter mAdapterInterface) {
        super(fm);
        if (listCategory != null) {

            mListCategories.addAll(listCategory);
        }
        if (mAdapterInterface != null){

            mICategoriesAdapter = mAdapterInterface;
        }
    }

    @Override
    public Fragment getItem(int position) {// tao list fragment cua adapter viewpager

        List<Category> cat_debit_loan = new ArrayList<>();

        List<Category> cat_expence = new ArrayList<>();

        List<Category> cat_income = new ArrayList<>();

        for (Category category : mListCategories) {

            if (category.getCategoryType().getValue() == 3 || category.getCategoryType().getValue() == 4) {

                cat_debit_loan.add(category);
            }
            if (category.getCategoryType().getValue() == 1) {

                cat_expence.add(category);
            }
            if (category.getCategoryType().getValue() == 2) {
                cat_income.add(category);
            }
        }

        switch (position) {

            case 0: {
                return new FragmentListCategories(cat_debit_loan, mCategoriesInterface);
            }
            case 1: {
                return new FragmentListCategories(cat_expence, mCategoriesInterface);
            }
            case 2: {
                return new FragmentListCategories(cat_income, mCategoriesInterface);
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {// title tablayout

        return titles[position];
    }

}
