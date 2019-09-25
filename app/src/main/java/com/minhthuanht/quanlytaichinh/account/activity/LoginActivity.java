package com.minhthuanht.quanlytaichinh.account.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.MainActivity;
import com.minhthuanht.quanlytaichinh.implementDAO.IWalletsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.WalletsDAOimpl;
import com.minhthuanht.quanlytaichinh.wallet.ChooseCurrencyActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextInputLayout mInputEmail;

    private TextInputLayout mInputPassword;

    private Button mLogin;

    private Button mRegistration;

    private Button mForgotPassword;

    private FirebaseAuth mAuth;

    private final View.OnClickListener mLoginListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View view) {

            loginHomeUser();

        }
    };


    private final View.OnClickListener mRegistrationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            sendRegistrationActivity();
        }
    };


    private final View.OnClickListener mForgotPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            sendForgotPasswordActivity();

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        initControls();
        initEvents();
    }

    private void initEvents() {

        mLogin.setOnClickListener(mLoginListener);
        mRegistration.setOnClickListener(mRegistrationListener);
        mForgotPassword.setOnClickListener(mForgotPasswordListener);

    }

    private void initControls() {

        mInputEmail = findViewById(R.id.txtInputEmail);
        mInputPassword = findViewById(R.id.txtInputPassword);
        mLogin = findViewById(R.id.btnLogin);
        mRegistration = findViewById(R.id.btnRegistration);
        mForgotPassword = findViewById(R.id.btnForgotPassWord);

    }

    private void sendRegistrationActivity() {

        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);

    }

    private void sendForgotPasswordActivity() {

        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loginHomeUser() {

        View review = null;

        String email = Objects.requireNonNull(mInputEmail.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(mInputPassword.getEditText()).getText().toString().trim();

        if (!isNetworkConnected()) {
            Toast.makeText(LoginActivity.this, getString(R.string.show_error_connect_internet), Toast.LENGTH_LONG).show();
        } else {

            if (email.isEmpty()) {
                mInputEmail.setError(getResources().getString(R.string.show_error_empty));
                review = mInputEmail;

            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mInputEmail.setError(getResources().getString(R.string.show_error_email_illegal));
                review = mInputEmail;
            }

            if (password.isEmpty()) {
                mInputPassword.setError(getResources().getString(R.string.show_error_empty));
                if (review == null) {
                    review = mInputPassword;
                }

            } else if (password.length() < 8) {
                mInputPassword.setError(getResources().getString(R.string.show_error_length_password));
                if (review == null) {
                    review = mInputPassword;
                }
            }

            if (review != null) {
                review.requestFocus();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });

        }
    }

    private void updateUI(FirebaseUser user) {

        // vào trang chính của user đó...

        if (user != null){

            //check wallet
            IWalletsDAO iWalletsDAO = new WalletsDAOimpl(this);

            if (iWalletsDAO.hasWallet(user.getUid())){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(LoginActivity.this, ChooseCurrencyActivity.class);
                startActivity(intent);
            }
        }


    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;

    }
}
