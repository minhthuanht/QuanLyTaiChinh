package com.minhthuanht.quanlytaichinh.transaction.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.implementDAO.BudgetDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.IBudgetDAO;
import com.minhthuanht.quanlytaichinh.model.Budget;
import com.minhthuanht.quanlytaichinh.wallet.adapter.WalletListAdapter;
import com.minhthuanht.quanlytaichinh.category.activity.SelectCategoryActivity;
import com.minhthuanht.quanlytaichinh.implementDAO.ITransactionsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.IWalletsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.TransactionsDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.WalletsDAOimpl;
import com.minhthuanht.quanlytaichinh.model.Category;
import com.minhthuanht.quanlytaichinh.model.MTDate;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.model.Wallet;
import com.minhthuanht.quanlytaichinh.view.CurrencyEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddTransactionActivity extends AppCompatActivity {

//    private static final String TAG = "AddTransactionActivity";

    private static final int REQEST_CODE_CATEGORY = 1;

    private CurrencyEditText mTransactionMoney;

    private EditText mTransactionCategory;

    private EditText mTransactionNote;

    private EditText mTransactionDate;

    private EditText mTransactionWallet;

    private ImageView mChoosePicture;

    private ImageView mCapturePicture;

    private ImageView mImgCategory;

    private ITransactionsDAO iTransactionsDAO;

    private IWalletsDAO iWalletsDAO;

    private Category mCategory;

    private Calendar mCalender;

    private List<Wallet> mListWallet = new ArrayList<>();

    private Wallet mCurrentWallet = null;


    private final View.OnClickListener mTransactionCategotyListener = view -> selectCategory();

    private final View.OnClickListener mTransactionDateListener = view -> selectDate();

    private final View.OnClickListener mTransactionWalletListener = view -> selectWallet();


    private final View.OnClickListener mChoosePictureListener = view -> choosePicture();

    private final View.OnClickListener mCapturePictureListener = view -> capturePicture();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initControls();
        initEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_save) {

            saveTransaction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {

        finishActivity();
        return true;
    }

    private void initControls() {

        mTransactionMoney = findViewById(R.id.txtTransactionMoney);
        mTransactionCategory = findViewById(R.id.txtTransactionCategory);
        mTransactionNote = findViewById(R.id.txtTransactionNote);
        mTransactionDate = findViewById(R.id.txtTransactionDate);
        mTransactionWallet = findViewById(R.id.txtTransactionWallet);
        mChoosePicture = findViewById(R.id.imgChoosePicture);
        mCapturePicture = findViewById(R.id.imgCapturePicture);
        mImgCategory = findViewById(R.id.imgTransactionCategory);
        mCalender = Calendar.getInstance();
        iWalletsDAO = new WalletsDAOimpl(this);
        mListWallet = iWalletsDAO.getAllWalletByUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private void initEvents() {

        mTransactionCategory.setOnClickListener(mTransactionCategotyListener);
        mTransactionDate.setOnClickListener(mTransactionDateListener);
        mTransactionWallet.setOnClickListener(mTransactionWalletListener);
        mChoosePicture.setOnClickListener(mChoosePictureListener);
        mCapturePicture.setOnClickListener(mCapturePictureListener);
    }

    private void finishActivity() {

        finish();
    }


    private void selectCategory() {

        Intent intent = new Intent(AddTransactionActivity.this, SelectCategoryActivity.class);
        startActivityForResult(intent, REQEST_CODE_CATEGORY);
    }


    private void selectDate() {

        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, dayofMonth) -> {

            mCalender.set(year, month, dayofMonth);
            updateLabelDate();
        };
        int year = mCalender.get(Calendar.YEAR);
        int month = mCalender.get(Calendar.MONTH);
        int dayofMonth = mCalender.get(Calendar.DATE);
        new DatePickerDialog(this, dateSetListener, year, month, dayofMonth).show();
    }

    private void updateLabelDate() {

        Date date = mCalender.getTime();
        if (DateUtils.isToday(date.getTime())) {

            mTransactionDate.setText(getString(R.string.date_today));
        } else {

            String strDate = new MTDate(mCalender).toIsoDateShortTimeString();
            mTransactionDate.setText(strDate);
        }
    }


    private void selectWallet() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.ic_account_balance_wallet_black_24dp);
        builderSingle.setTitle(getResources().getString(R.string.select_wallet));

        final ArrayAdapter arrayAdapter = new WalletListAdapter(this, R.layout.item_wallet, mListWallet);


        builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            mCurrentWallet = mListWallet.get(which);
            updateUI();
            dialog.dismiss();
        });
        builderSingle.show();
    }

    private void choosePicture() {

        Toast.makeText(this, getString(R.string.notification_system_developing), Toast.LENGTH_SHORT).show();
    }


    private void capturePicture() {

        Toast.makeText(this, getString(R.string.notification_system_developing), Toast.LENGTH_SHORT).show();
    }

    private void saveTransaction() {

        float money = (float) mTransactionMoney.getCleanDoubleValue();
        String note = mTransactionNote.getText().toString();
        Date date = mCalender.getTime();

        if (mCategory == null) {

            mTransactionCategory.setError("Vui lòng chọn giao dịch");
            mTransactionCategory.requestFocus();
            return;
        }
        if (mCurrentWallet == null) {

            mTransactionWallet.setError("Vui lòng Chọn ví");
            mTransactionWallet.requestFocus();
            return;
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionCurrency(mCurrentWallet.getWalletCurrency());
        transaction.setTransactionTrading(money);
        transaction.setTransactionDate(date);
        transaction.setTransactionNote(note);
        transaction.setTransactionCategoryID(mCategory);
        transaction.setTrasactionWalletID(mCurrentWallet);

        iTransactionsDAO = new TransactionsDAOimpl(this);
        iTransactionsDAO.insertTransaction(transaction);

        updaBalanceWallet();

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.add_transaction_complete), transaction);
        intent.putExtra(getResources().getString(R.string.complete), bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void updaBalanceWallet() {

        float balance = 0;

        iWalletsDAO = new WalletsDAOimpl(this);

        List<Transaction> transactions = iTransactionsDAO.getAllTransactionByWalletId(mCurrentWallet.getWalletID());

        for (Transaction tran : transactions) {

            balance += tran.getMoneyTradingWithSign();
        }

        mCurrentWallet.setWalletBalance(balance);
        iWalletsDAO.updateWallet(mCurrentWallet);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQEST_CODE_CATEGORY) {

            if (resultCode == RESULT_OK) {

                assert data != null;
                mCategory = data.getParcelableExtra(SelectCategoryActivity.RESULT_SELECT_CATEGORY);
            }

            updateUI();
        }
    }

    private void updateUI() {

        if (mCategory != null) {

            mTransactionCategory.setText(mCategory.getCategory());

            // lấy ảnh từ asset
            String path_icon = "category/";
            try {

                Drawable drawable = Drawable.createFromStream(this.getAssets().open(path_icon + mCategory.getCategoryIcon()), null);
                mImgCategory.setImageDrawable(drawable);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mCurrentWallet != null) {

            mTransactionWallet.setText(mCurrentWallet.getWalletName());
        }
    }

}
