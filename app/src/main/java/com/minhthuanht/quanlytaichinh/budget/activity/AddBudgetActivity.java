package com.minhthuanht.quanlytaichinh.budget.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.category.activity.SelectCategoryActivity;
import com.minhthuanht.quanlytaichinh.implementDAO.BudgetDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.IBudgetDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.ITransactionsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.IWalletsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.TransactionsDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.WalletsDAOimpl;
import com.minhthuanht.quanlytaichinh.model.Budget;
import com.minhthuanht.quanlytaichinh.model.Category;
import com.minhthuanht.quanlytaichinh.model.DateRange;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.model.Wallet;
import com.minhthuanht.quanlytaichinh.utilities.DateUtils;
import com.minhthuanht.quanlytaichinh.view.CurrencyEditText;
import com.minhthuanht.quanlytaichinh.wallet.adapter.WalletListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class AddBudgetActivity extends AppCompatActivity {

    private static final int CATEGORY_REQUEST = 1;

    private static final int TIME_REQUEST = 2;

    public static final String RESULT_ADD = "add_complete";

    private ImageView mImgCategory;

    private TextView mCategoryBudget;

    private CurrencyEditText mMoneyBudget;

    private TextView mTimeBudget;

    private TextView mWalletBudget;

    private CheckBox mCheckLoop;

    private Calendar mCalendar = Calendar.getInstance();

    private List<Wallet> mListWallet = new ArrayList<>();

    private Wallet mCurrentWallet = new Wallet();

    private Category mCategory = new Category();

    private boolean mChecked;

    private DateRange mDateRange;

    private final View.OnClickListener mCategoryBudgetListener = view -> selectCategory();

    private final View.OnClickListener mTimeBudgetListener = view -> selectTimeBudget();

    private final View.OnClickListener mWalletBudgetListener = view -> selectWallet();

    private final CompoundButton.OnCheckedChangeListener mCheckLoopListener = (compoundButton, b) -> {
        checkLoopBudget(b);
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initControls();
        initEvents();
    }

    private void initControls() {

        mImgCategory = findViewById(R.id.imgCategory);
        mCategoryBudget = findViewById(R.id.txtCategoryBudget);
        mMoneyBudget = findViewById(R.id.txtMoneyBudget);
        mTimeBudget = findViewById(R.id.txtDateBudget);
        mWalletBudget = findViewById(R.id.txtBudgetWallet);
        mCheckLoop = findViewById(R.id.chkLoop);

        IWalletsDAO iWalletsDAO = new WalletsDAOimpl(this);
        mListWallet = iWalletsDAO.getAllWalletByUser(FirebaseAuth.getInstance().getCurrentUser().getUid());


    }

    private void initEvents() {

        mCategoryBudget.setOnClickListener(mCategoryBudgetListener);
        mTimeBudget.setOnClickListener(mTimeBudgetListener);
        mWalletBudget.setOnClickListener(mWalletBudgetListener);
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

    private void selectWallet() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_account_balance_wallet_black_24dp);
        builder.setTitle(getResources().getString(R.string.select_wallet));

        ArrayAdapter arrayAdapter = new WalletListAdapter(this, R.layout.item_wallet, mListWallet);
        builder.setNegativeButton("Đóng", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        builder.setAdapter(arrayAdapter, (dialogInterface, i) -> {
            mCurrentWallet = mListWallet.get(i);

            updateUI();
            dialogInterface.dismiss();
        });

        builder.show();

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

        if (mCurrentWallet != null) {

            mWalletBudget.setText(mCurrentWallet.getWalletName());
        }

    }

    private void saveAddBudget() {

        DateUtils dateUtils = new DateUtils();

        float money = (float) mMoneyBudget.getCleanDoubleValue();

        Budget budget = new Budget();

        budget.setAmount(money);

        if (!mCategoryBudget.getText().toString().isEmpty()) {

            budget.setCategory(mCategory);
        } else {

            mCategoryBudget.setError("Vui lòng chọn Nhóm");
            mCategoryBudget.requestFocus();
            return;
        }

        if (!mTimeBudget.getText().toString().isEmpty()) {

            budget.setTimeStart(mDateRange.getDateFrom());
            budget.setTimeEnd(mDateRange.getDateTo());
        } else {

            mTimeBudget.setError("Vui lòng chọn thời gian");
            mTimeBudget.requestFocus();
            return;
        }

        if (!mWalletBudget.getText().toString().isEmpty()) {

            budget.setWallet(mCurrentWallet);
        } else {

            mWalletBudget.setError("Vui lòng chọn ví");
            mWalletBudget.requestFocus();
            return;
        }

        budget.setSpent(0);
        budget.setLoop(mChecked);
        budget.setStatus("Create Budget");


        IBudgetDAO iBudgetDAO = new BudgetDAOimpl(this);

        iBudgetDAO.insertBudget(budget);

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_ADD,budget);
        intent.putExtra(RESULT_ADD, bundle);
        setResult(RESULT_OK, intent);

        finish();

    }


}
