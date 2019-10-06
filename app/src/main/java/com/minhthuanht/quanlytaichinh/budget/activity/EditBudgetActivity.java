package com.minhthuanht.quanlytaichinh.budget.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.category.activity.SelectCategoryActivity;
import com.minhthuanht.quanlytaichinh.implementDAO.BudgetDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.IBudgetDAO;

import com.minhthuanht.quanlytaichinh.model.Budget;
import com.minhthuanht.quanlytaichinh.model.Category;
import com.minhthuanht.quanlytaichinh.model.DateRange;

import com.minhthuanht.quanlytaichinh.view.CurrencyEditText;


import java.io.IOException;

import java.util.Objects;

public class EditBudgetActivity extends AppCompatActivity {

    private static final int CATEGORY_REQUEST = 1;

    private static final int TIME_REQUEST = 2;

    private static final String CODE_SEND_EDIT_BUDGET = "sendEditBudgetActivity";

    public static final String RESULT_EDIT = "edit_complete";

    private Budget mBudget = new Budget();

    private ImageView mImgCategory;

    private ImageView mImgWalletBudget;

    private TextView mCategoryBudget;

    private CurrencyEditText mMoneyBudget;

    private TextView mTimeBudget;

    private TextView mWalletBudget;

    private CheckBox mCheckLoop;

    private Category mCategory = new Category();

    private boolean mChecked;

    private DateRange mDateRange;

    private final View.OnClickListener mCategoryBudgetListener = view -> selectCategory();

    private final View.OnClickListener mTimeBudgetListener = view -> selectTimeBudget();

    private final CompoundButton.OnCheckedChangeListener mCheckLoopListener = (compoundButton, b) -> checkLoopBudget(b);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initData();
        initControls();
        initEvents();
    }

    private void initData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(CODE_SEND_EDIT_BUDGET);
        assert bundle != null;
        mBudget = bundle.getParcelable(CODE_SEND_EDIT_BUDGET);

    }

    private void initControls() {

        mImgCategory = findViewById(R.id.imgCategory);
        mCategoryBudget = findViewById(R.id.txtCategoryBudget);
        mMoneyBudget = findViewById(R.id.txtMoneyBudget);
        mTimeBudget = findViewById(R.id.txtDateBudget);
        mWalletBudget = findViewById(R.id.txtBudgetWallet);
        mImgWalletBudget = findViewById(R.id.image_transaction_wallet);
        mCheckLoop = findViewById(R.id.chkLoop);

        onBindView();


    }

    private void onBindView() {

        mCategory = mBudget.getCategory();
        String path_icon = "category/";
        try {
            Drawable drawable = Drawable.createFromStream(getAssets().open(path_icon + mBudget.getCategory().getCategoryIcon()), null);
            mImgCategory.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }


        mCategoryBudget.setText(mBudget.getCategory().getCategory());
        mMoneyBudget.setText(String.valueOf((int) mBudget.getAmount()));
        mDateRange = new DateRange(mBudget.getTimeStart(), mBudget.getTimeEnd());
        mTimeBudget.setText(mDateRange.toString());

        try {
            Drawable drawable = Drawable.createFromStream(getAssets().open(path_icon + "icon.png"), null);
            mImgWalletBudget.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mWalletBudget.setText(mBudget.getWallet().getWalletName());

    }

    private void initEvents() {

        mCategoryBudget.setOnClickListener(mCategoryBudgetListener);
        mTimeBudget.setOnClickListener(mTimeBudgetListener);
        mCheckLoop.setOnCheckedChangeListener(mCheckLoopListener);

    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_budget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_budget) {

            saveAddBudget();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CATEGORY_REQUEST && resultCode == RESULT_OK) {

            assert data != null;
            mCategory = data.getParcelableExtra(SelectCategoryActivity.RESULT_SELECT_CATEGORY);
        }
        if (requestCode == TIME_REQUEST && resultCode == RESULT_OK) {

            assert data != null;
            mDateRange = (DateRange) data.getSerializableExtra(SelectTimeActivity.CODE_RESPONSE);
        }

        updateUI();
    }

    private void checkLoopBudget(boolean b) {

        mChecked = b;
    }


    private void selectTimeBudget() {

        Intent intent = new Intent(this, SelectTimeActivity.class);
        startActivityForResult(intent, TIME_REQUEST);

    }

    private void selectCategory() {

        Intent intent = new Intent(this, SelectCategoryActivity.class);
        startActivityForResult(intent, CATEGORY_REQUEST);

    }

    private void updateUI() {

        if (mCategory != null) {

            mCategoryBudget.setText(mCategory.getCategory());

            String path_icon = "category/";
            try {
                Drawable drawable = Drawable.createFromStream(this.getAssets().open(path_icon + mCategory.getCategoryIcon()), null);
                mImgCategory.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (mDateRange != null) {

            mTimeBudget.setText(mDateRange.toString());
        }

    }

    private void saveAddBudget() {


        float money = (float) mMoneyBudget.getCleanDoubleValue();

        Budget budget = new Budget();

        budget.setBudgetId(mBudget.getBudgetId());

        budget.setAmount(money);

        if (!mCategoryBudget.getText().toString().isEmpty()) {

            budget.setCategory(mCategory);
        }

        if (!mTimeBudget.getText().toString().isEmpty()) {

            budget.setTimeStart(mDateRange.getDateFrom());
            budget.setTimeEnd(mDateRange.getDateTo());
        }

        budget.setWallet(mBudget.getWallet());

        budget.setSpent(mBudget.getSpent());
        budget.setLoop(mChecked);
        budget.setStatus("Edit Budget");


        IBudgetDAO iBudgetDAO = new BudgetDAOimpl(this);

        iBudgetDAO.updateBudget(budget);

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_EDIT, budget);
        intent.putExtra(RESULT_EDIT, bundle);
        setResult(RESULT_OK, intent);

        finish();

    }

}
