package com.minhthuanht.quanlytaichinh.account.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.minhthuanht.quanlytaichinh.R;


import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputLayout mInputEmail;

    private TextInputLayout mInputPassword;

    private Button mRegistration;

    private Button mLogin;

    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;

    private static final String TAG = "REGISTRATION";

    private View.OnClickListener mRegistrationListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View view) {
            createNewAccount();
        }
    };


    private View.OnClickListener mLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sendLoginActivity();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        initControls();
        intitEvents();

    }

    private void intitEvents() {

        mRegistration.setOnClickListener(mRegistrationListener);
        mLogin.setOnClickListener(mLoginListener);

    }

    private void initControls() {

        mInputEmail = findViewById(R.id.txtInputEmail);
        mInputPassword = findViewById(R.id.txtInputPassword);
        mRegistration = findViewById(R.id.btnRegistration);
        mLogin = findViewById(R.id.btnLogin);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getResources().getString(R.string.show_title_prodialog));
        mProgressDialog.setMessage(getResources().getString(R.string.show_title_registration));
        mProgressDialog.setCanceledOnTouchOutside(false);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createNewAccount() {

        View review = null;

        String email = Objects.requireNonNull(mInputEmail.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(mInputPassword.getEditText()).getText().toString().trim();

        if (!isNetworkConnected()) {
            Toast.makeText(RegistrationActivity.this, getString(R.string.show_error_connect_internet), Toast.LENGTH_LONG).show();
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

            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, getResources().getString(R.string.show_complete_registration));
                                mProgressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.show_complete_registration), Toast.LENGTH_LONG).show();
                                sendLoginActivity();
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, getResources().getString(R.string.show_error_registration_fail), task.getException());
                                mProgressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.show_error_registration_fail),
                                        Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                            }

                            // ...
                        }
                    });

        }

    }

    private void sendLoginActivity() {

        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }
}


