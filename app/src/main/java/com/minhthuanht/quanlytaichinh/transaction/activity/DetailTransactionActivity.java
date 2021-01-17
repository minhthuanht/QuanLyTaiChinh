package com.minhthuanht.quanlytaichinh.transaction.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.implementDAO.ITransactionsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.IWalletsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.TransactionsDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.WalletsDAOimpl;
import com.minhthuanht.quanlytaichinh.model.MTDate;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.model.Wallet;

import java.io.IOException;
import java.util.Objects;

public class DetailTransactionActivity extends AppCompatActivity {

    private static final int REQUESR_EDIT_TRANSACTION = 111;

    private TextView mTransactionMoney;

    private TextView mTransactionCategory;

    private TextView mTransactionNote;

    private TextView mTransactionDate;

    private TextView mTransactionWallet;
//
//    private ImageView mChoosePicture;
//
//    private ImageView mCapturePicture;

    private ImageView mImgCategory;

    private Transaction mTransaction = new Transaction();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initData();
        initControls();
        initEvents();
    }

    private void initData() {

        Intent intent = getIntent();
        if (intent != null) {

            mTransaction = intent.getParcelableExtra("transactionItem");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initControls() {

        mTransactionMoney = findViewById(R.id.txtTransactionMoney);
        mTransactionCategory = findViewById(R.id.txtTransactionCategory);
        mTransactionNote = findViewById(R.id.txtTransactionNote);
        mTransactionDate = findViewById(R.id.txtTransactionDate);
        mTransactionWallet = findViewById(R.id.txtTransactionWallet);
//        mChoosePicture = findViewById(R.id.imgChoosePicture);
//        mCapturePicture = findViewById(R.id.imgCapturePicture);
        mImgCategory = findViewById(R.id.imgTransactionCategory);

        onBindView(mTransaction);
    }

    private void onBindView(Transaction transaction) {

        if (transaction != null) {

            String money = String.valueOf(transaction.getTransactionTrading());
            mTransactionMoney.setText(money);
            if (transaction.getMoneyTradingWithSign() >= 0) {
                mTransactionMoney.setTextColor(getResources().getColor(R.color.colorMoneyTradingPositive));
            } else {
                mTransactionMoney.setTextColor(getResources().getColor(R.color.colorMoneyTradingNegative));
            }
            mTransactionCategory.setText(transaction.getTransactionCategoryID().getCategory());
            // lấy ảnh
            String path_icon = "category/";
            try {
                Drawable drawable = Drawable.createFromStream(getAssets()
                        .open(path_icon + transaction.getTransactionCategoryID().getCategoryIcon()), null);
                mImgCategory.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mTransactionNote.setText(transaction.getTransactionNote());
            mTransactionDate.setText(new MTDate(transaction.getTransactionDate()).toIsoDateShortTimeString());
            mTransactionWallet.setText(transaction.getTransactionWalletID().getWalletName());
        }
    }

    private void initEvents() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESR_EDIT_TRANSACTION) {

            if (resultCode == RESULT_OK) {

                assert data != null;
                mTransaction = data.getParcelableExtra(getResources().getString(R.string.complete));
                onBindView(mTransaction);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.meunu_transaction_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_delete) {

            deleteTransaction();

        }
        if (item.getItemId() == R.id.action_edit) {

            editTransaction();
        }
        return super.onOptionsItemSelected(item);
    }

    private void editTransaction() {

        Intent intent = new Intent(this, EditTransactionActivity.class);
        intent.putExtra("transaction", mTransaction);
        startActivityForResult(intent, REQUESR_EDIT_TRANSACTION);
    }

    private void deleteTransaction() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.title_delete_transaction));
        builder.setMessage(getResources().getString(R.string.messing_delete));
        builder.setPositiveButton(getResources().getString(R.string.comfirm), (dialogInterface, i) -> {
            ITransactionsDAO iTransactionsDAO = new TransactionsDAOimpl(DetailTransactionActivity.this);
            iTransactionsDAO.deleteTransaction(mTransaction);
            setResult(RESULT_OK);

            IWalletsDAO iWalletsDAO = new WalletsDAOimpl(DetailTransactionActivity.this);

            Wallet wallet = mTransaction.getTransactionWalletID();

            float balanceCurent = wallet.getWalletBalance();

            if (mTransaction.getMoneyTradingWithSign() < 0) {

                balanceCurent += mTransaction.getTransactionTrading();
            } else {

                balanceCurent -= mTransaction.getTransactionTrading();
            }

            wallet.setWalletBalance(balanceCurent);
            iWalletsDAO.updateWallet(wallet);
            finish();
        });
        builder.setNegativeButton(getResources().getString(R.string.no), null);
        builder.show();
    }
}
