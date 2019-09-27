package com.minhthuanht.quanlytaichinh.category.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.category.adapter.CategoryItemAdapter;
import com.minhthuanht.quanlytaichinh.model.Category;

import java.util.ArrayList;
import java.util.List;

public class FragmentListCategories extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public interface IFragmentListCategories {

        void onItemClicked(Category category);
    }

    private IFragmentListCategories mCategoriesInterface;

    private final CategoryItemAdapter.ICategoryAdapterListener mICategoryAdapter = new CategoryItemAdapter.ICategoryAdapterListener() {
        @Override
        public void onItemClicked(Category category) {

            mCategoriesInterface.onItemClicked(category);
        }
    };

    private List<Category> mListCategories = new ArrayList<>();

    public FragmentListCategories() {

    }

    public FragmentListCategories(List<Category> listCategories, IFragmentListCategories categoriesInterface) {

        if (listCategories != null && categoriesInterface != null) {

            this.mCategoriesInterface = categoriesInterface;
            this.mListCategories = listCategories;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_categories, container, false);
        RecyclerView mViewCategory = view.findViewById(R.id.viewCategory);
        mViewCategory.setHasFixedSize(true);
        mViewCategory.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);

        mViewCategory.setLayoutManager(llm);
        CategoryItemAdapter mAdapter = new CategoryItemAdapter(mListCategories, mICategoryAdapter);
        mViewCategory.setAdapter(mAdapter);

        return view;
    }

}
