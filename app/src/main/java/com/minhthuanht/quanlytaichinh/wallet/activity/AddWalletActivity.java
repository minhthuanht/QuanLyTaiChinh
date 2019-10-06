package com.minhthuanht.quanlytaichinh.wallet.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputLayout;
import com.minhthuanht.quanlytaichinh.R;

import java.util.Objects;

public class AddWalletActivity extends AppCompatActivity {

    private static final String TAG = "AddWalletActivity";

    private ImageButton mImgBack;

    private TextInputLayout mInputWallet;

    private Button mContinue;

    private String mCurrency;

    private View.OnClickListener mImgBackListener = view -> backChooseCurrencyActivity();

    private View.OnClickListener mContinueListener = view -> sendBalanceActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);

        getDataChooseCurrency();

        initControls();
        initEvents();
    }


    private void initEvents() {

        mImgBack.setOnClickListener(mImgBackListener);
        mContinue.setOnClickListener(mContinueListener);
    }

    @SuppressLint("CutPasteId")
    private void initControls() {

        mImgBack = findViewById(R.id.imgBack);
        mInputWallet = findViewById(R.id.txtInputWallet);
        mContinue = findViewById(R.id.btnContinue);
    }

    private void getDataChooseCurrency() {

        Intent intent = getIntent();
        mCurrency = intent.getStringExtra("CURRENCY");
        assert mCurrency != null;
        Log.d(TAG, mCurrency);
    }

    private void backChooseCurrencyActivity() {

        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendBalanceActivity() {

        String nameWallet = Objects.requireNonNull(mInputWallet.getEditText()).getText().toString();

        if (nameWallet.isEmpty()) {

            mInputWallet.setError(getString(R.string.show_error_empty));
            mInputWallet.requestFocus();
        } else {

            Intent intent = new Intent(AddWalletActivity.this, BalanceActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("CURRENCY", mCurrency);
            bundle.putString("NAME_WALLET", nameWallet);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
