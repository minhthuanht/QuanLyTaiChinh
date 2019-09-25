package com.minhthuanht.quanlytaichinh.account.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.minhthuanht.quanlytaichinh.R;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";

    private EditText mInputEmail;

    private Button mSend;

    private FirebaseAuth mAuth;

    private final View.OnClickListener mSendListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View view) {

            resetPasswordByEmail();

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        initControls();
        initEvents();
    }

    private void initEvents() {

        mSend.setOnClickListener(mSendListener);
    }

    private void initControls() {

        mInputEmail = findViewById(R.id.txtEmail);
        mSend = findViewById(R.id.btnSend);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void resetPasswordByEmail() {

        View review = null;
        String email = Objects.requireNonNull(mInputEmail.getText().toString()).trim();
        if (!isNetworkConnected()) {
            Toast.makeText(ForgotPasswordActivity.this, getString(R.string.show_error_connect_internet), Toast.LENGTH_LONG).show();
        } else {

            if (email.isEmpty()) {
                mInputEmail.setError(getResources().getString(R.string.show_error_empty));
                review = mInputEmail;

            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mInputEmail.setError(getResources().getString(R.string.show_error_email_illegal));
                review = mInputEmail;
            }

            if (review != null) {
                review.requestFocus();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, getResources().getString(R.string.show_notification_sent_change_password_to_email));
                                Toast.makeText(ForgotPasswordActivity.this, getString(R.string.show_notification_send_email), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;

    }
}
