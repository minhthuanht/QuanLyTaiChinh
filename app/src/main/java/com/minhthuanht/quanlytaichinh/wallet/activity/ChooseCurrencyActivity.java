package com.minhthuanht.quanlytaichinh.wallet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.account.activity.LoginActivity;

public class ChooseCurrencyActivity extends AppCompatActivity {

    private static final String TAG = "ChooseCurrencyActivity";

    private ImageButton mImgBack;

    private Spinner mSpinnerCurrency;

    private Button mContinue;

    private String mCurrency;

    private View.OnClickListener mImgBackListener = view -> backLoginActivity();

    private View.OnClickListener mContinueListener = view -> sendAddWalletActivity();

    private AdapterView.OnItemSelectedListener mSpinnerCurrencyListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.p_750));
            mCurrency = mSpinnerCurrency.getSelectedItem().toString();

            Log.d(TAG, mCurrency);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_currency);

        initControls();
        initEvents();
    }

    private void initEvents() {

        mImgBack.setOnClickListener(mImgBackListener);
        mSpinnerCurrency.setOnItemSelectedListener(mSpinnerCurrencyListener);
        mContinue.setOnClickListener(mContinueListener);
    }

    private void initControls() {

        mImgBack = findViewById(R.id.imgBack);
        mContinue = findViewById(R.id.btnContinue);
        mSpinnerCurrency = findViewById(R.id.spinnerCurrency);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.curency, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCurrency.setAdapter(adapter);
    }

    private void sendAddWalletActivity() {

        Intent intent = new Intent(ChooseCurrencyActivity.this, AddWalletActivity.class);
        intent.putExtra("CURRENCY", mCurrency);
        startActivity(intent);
    }

    private void backLoginActivity() {

        Intent intent = new Intent(ChooseCurrencyActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
