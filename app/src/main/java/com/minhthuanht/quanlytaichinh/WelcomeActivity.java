package com.minhthuanht.quanlytaichinh;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.minhthuanht.quanlytaichinh.account.activity.LoginActivity;
import com.minhthuanht.quanlytaichinh.account.activity.RegistrationActivity;
import com.minhthuanht.quanlytaichinh.implementDAO.IWalletsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.WalletsDAOimpl;
import com.minhthuanht.quanlytaichinh.wallet.ChooseCurrencyActivity;

public class WelcomeActivity extends AppCompatActivity {

    private Spinner mSpinerLanguage;

    private Button mUseFirstApp;

    private Button mUsedApp;

    private ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;

    private final View.OnClickListener mUseFirstAppClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(WelcomeActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }
    };

    private final View.OnClickListener mUsedAppClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    private final AdapterView.OnItemSelectedListener mSpinerLanguageSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.p_750));
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();


        initViews();
        initEvents();
        updateUI();
    }

    private void updateUI() {

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            IWalletsDAO iWalletsDAO = new WalletsDAOimpl(this);

            if (iWalletsDAO.hasWallet(user.getUid())) {

                mProgressDialog.show();
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                mProgressDialog.dismiss();

                finish();
            } else {

                mProgressDialog.show();
                Intent intent = new Intent(WelcomeActivity.this, ChooseCurrencyActivity.class);
                startActivity(intent);
                mProgressDialog.dismiss();
            }

        }

    }


    private void initEvents() {
        mSpinerLanguage.setOnItemSelectedListener(mSpinerLanguageSelected);

        mUseFirstApp.setOnClickListener(mUseFirstAppClicked);

        mUsedApp.setOnClickListener(mUsedAppClicked);
    }

    private void initViews() {
        mSpinerLanguage = findViewById(R.id.spinerLanguage);
        mUseFirstApp = findViewById(R.id.btnUseFirstApp);
        mUsedApp = findViewById(R.id.btnUsedApp);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getResources().getString(R.string.titile_notification));
        mProgressDialog.setMessage(getResources().getString(R.string.notification_wait));
        mProgressDialog.setCanceledOnTouchOutside(false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.language, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinerLanguage.setAdapter(adapter);
    }

}
