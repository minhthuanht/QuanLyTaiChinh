package com.minhthuanht.quanlytaichinh.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.MainActivity;
import com.minhthuanht.quanlytaichinh.implementDAO.ITransactionsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.IWalletsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.TransactionsDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.WalletsDAOimpl;
import com.minhthuanht.quanlytaichinh.model.Category;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.model.TransactionTypes;
import com.minhthuanht.quanlytaichinh.model.Wallet;

import java.util.Calendar;

public class FinishUpWallet extends AppCompatActivity {

    private static final String TAG = "FinishUpWallet";

    private ImageButton mImgBack;

    private TextView mNameWallet;

    private TextView mBalanceWallet;

    private Button mDone;

    private String currency;

    private String nameWallet;

    private float balance;

    private FirebaseAuth mAuth;

    private View.OnClickListener mImgBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            backBalanceActivity();
        }
    };

    private View.OnClickListener mDoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            sendMainActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_up_wallet);

        mAuth = FirebaseAuth.getInstance();

        getDataBalanceActivity();

        initControls();
        initEvents();

        updateUI();
    }

    private void getDataBalanceActivity() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            currency = bundle.getString("CURRENCY");
            nameWallet = bundle.getString("NAME_WALLET");
            balance = bundle.getFloat("BALANCE");
        }
    }

    private void initControls() {

        mImgBack = findViewById(R.id.imgBack);
        ImageView mImgWallet = findViewById(R.id.imgWallet);
        mNameWallet = findViewById(R.id.txtNameWallet);
        mBalanceWallet = findViewById(R.id.txtBalance);
        mDone = findViewById(R.id.btnDone);
    }

    private void initEvents() {

        mImgBack.setOnClickListener(mImgBackListener);
        mDone.setOnClickListener(mDoneListener);
    }

    private void backBalanceActivity() {

        finish();
    }

    private void sendMainActivity() {


        // send mainactivity of user

        // save wallet in sql tbl_wallet, and tbl_transactions
        IWalletsDAO iWalletsDAO = new WalletsDAOimpl(this);
        ITransactionsDAO iTransactionsDAO = new TransactionsDAOimpl(this);
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String userId = user.getUid();
        Wallet wallet = new Wallet(nameWallet, balance, currency, userId);
        boolean add_wallet = iWalletsDAO.insertWallet(wallet);

        Calendar calendar = Calendar.getInstance();
        Category category = new Category();
        category.setCategoryID(56);
        category.setCategoryType(TransactionTypes.INCOME.getValue());
        Transaction transaction = new Transaction(currency, balance, calendar.getTime(), "Initial Wallet", category, wallet);
        boolean add_transaction = iTransactionsDAO.insertTransaction(transaction);

        if (add_wallet && add_transaction) {

            Intent intent = new Intent(FinishUpWallet.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {

            Toast.makeText(this, "Khởi tạo ví thất bại.", Toast.LENGTH_SHORT).show();

            if (add_wallet) {

                iWalletsDAO.deleteWallet(iWalletsDAO.getIDMax());
            } else if (add_transaction) {

                iTransactionsDAO.deleteTransaction(iTransactionsDAO.getTransactionById(iTransactionsDAO.getIDMax()));
            }
        }


    }

    private void updateUI() {

        mNameWallet.setText(nameWallet);
        mBalanceWallet.setText(String.format("$%s", balance));
    }
}
