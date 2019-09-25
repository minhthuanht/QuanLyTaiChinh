package com.minhthuanht.quanlytaichinh.wallet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

public class BalanceActivity extends AppCompatActivity {

    private static final String TAG = "BalanceActivity";

    private ImageButton mImgBack;

    private TextInputLayout mInputBalance;

    private Button mContinue;

    private Bundle bundle;

    private View.OnClickListener mImgBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            backAddWalletActivity();
        }
    };

    private View.OnClickListener mContinueListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View view) {

            sendFinishUpWallet();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        getDataAddWalletActivity();

        initControls();
        initEvents();
    }

    private void initEvents() {

        mImgBack.setOnClickListener(mImgBackListener);
        mContinue.setOnClickListener(mContinueListener);
    }

    private void initControls() {

        mImgBack = findViewById(R.id.imgBack);
        mInputBalance = findViewById(R.id.txtInputBalance);
        mContinue = findViewById(R.id.btnContinue);
    }

    private void getDataAddWalletActivity() {

        Intent intent = getIntent();
        bundle = intent.getExtras();
        Log.d(TAG, String.valueOf(bundle));
    }

    private void backAddWalletActivity() {

        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendFinishUpWallet() {

        String valueBalance = Objects.requireNonNull(mInputBalance.getEditText()).getText().toString();

        if (valueBalance.isEmpty()) {

            mInputBalance.setError(getString(R.string.show_error_empty));
            mInputBalance.requestFocus();
        } else {

            Intent intent = new Intent(BalanceActivity.this, FinishUpWallet.class);
            float balance = Float.parseFloat(valueBalance);
            if (bundle != null) {
                bundle.putFloat("BALANCE", balance);

                intent.putExtras(bundle);
            }
            startActivity(intent);
        }
    }

}
