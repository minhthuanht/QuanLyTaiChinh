package com.minhthuanht.quanlytaichinh.category.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.category.adapter.CategoriesPageAdapter;
import com.minhthuanht.quanlytaichinh.implementDAO.CategoriesDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.ICategoriesDAO;
import com.minhthuanht.quanlytaichinh.model.Category;

import java.util.List;
import java.util.Objects;

public class SelectCategoryActivity extends AppCompatActivity {

    private CategoriesPageAdapter.ICategoriesAdapter mAdapterInterface = category -> sendCategoryClicked(category);

    public static final String RESULT_SELECT_CATEGORY = "SelectCategoryActivity";

    private static final String TAG = "SelectCategoryActivity";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initControls();
        initEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_category) {

            Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    private void initControls() {

        PagerSlidingTabStrip mTabLayout = findViewById(R.id.tab_layout);
        ViewPager mViewPager = findViewById(R.id.viewPager);
        ICategoriesDAO mICategoriesDAO = new CategoriesDAOimpl(this);
        List<Category> mListCategories;
        mListCategories = mICategoriesDAO.getAllCategory();
        CategoriesPageAdapter mAdapter = new CategoriesPageAdapter(getSupportFragmentManager(), mListCategories, mAdapterInterface);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1);
        mTabLayout.setViewPager(mViewPager);

        Log.d(TAG, "SelectCategoryActivity");
    }

    private void initEvents() {

    }


    private void sendCategoryClicked(Category category) {

        Intent intent = getIntent();
        intent.putExtra(RESULT_SELECT_CATEGORY, category);
        setResult(RESULT_OK, intent);
        finish();
    }

}
