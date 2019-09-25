package com.minhthuanht.quanlytaichinh.account.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.account.activity.ForgotPasswordActivity;
import com.minhthuanht.quanlytaichinh.account.activity.LoginActivity;

import java.util.Objects;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FragmentChangePassword extends Fragment {

    private static final String TAG = "FragmentChangePassword";

    private TextInputLayout mInputPassword;

    private TextInputLayout mInputPassword2;

    private Button mChangePassword;

    private Button mForgotPassword;

    private FirebaseAuth mAuth;

    private final View.OnClickListener mChangePasswordListener = view -> changPassword();

    private final View.OnClickListener mForgotPasswordListener = view -> forgotPassword();

    public FragmentChangePassword() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        mInputPassword = view.findViewById(R.id.txtInputPassword);
        mInputPassword2 = view.findViewById(R.id.txtInputPassword2);
        mChangePassword = view.findViewById(R.id.btnChangePassWord);
        mForgotPassword = view.findViewById(R.id.btnForgotPassWord);

        mAuth = FirebaseAuth.getInstance();

        initEvents();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.change_password));
    }

    private void initEvents() {

        mChangePassword.setOnClickListener(mChangePasswordListener);
        mForgotPassword.setOnClickListener(mForgotPasswordListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void changPassword() {

        if (!isNetworkConnected()) {

            Toast.makeText(getContext(), getString(R.string.show_error_connect_internet), Toast.LENGTH_SHORT).show();
        } else {

            final String password = Objects.requireNonNull(mInputPassword.getEditText()).getText().toString();
            final String password2 = Objects.requireNonNull(mInputPassword2.getEditText()).getText().toString();

            if (mAuth != null) {

                if (password.isEmpty()) {

                    mInputPassword.setError(getString(R.string.show_error_empty));
                    mInputPassword.requestFocus();
                    return;
                }
                if (password2.isEmpty()) {

                    mInputPassword2.setError(getString(R.string.show_error_empty));
                    mInputPassword2.requestFocus();
                } else {

                    final FirebaseUser user = mAuth.getCurrentUser();

                    if (user != null) {

                        String email = user.getEmail();

                        assert email != null;
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email, password);

                        user.reauthenticate(credential)
                                .addOnCompleteListener(task -> {

                                    if (task.isSuccessful()) {

                                        Log.d(TAG, "auth success");

                                        user.updatePassword(password2).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {

                                                Log.d(TAG, "Password updated");

                                                Toast.makeText(getContext(), getString(R.string.change_password_complete), Toast.LENGTH_SHORT).show();
                                                updateUI();

                                            } else {

                                                Log.d(TAG, "Error password not updated");
                                                Toast.makeText(getActivity(), getString(R.string.change_password_not_complete), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    } else {

                                        try {
                                            throw Objects.requireNonNull(task.getException());

                                        } catch (FirebaseAuthRecentLoginRequiredException ex) {

                                            Toast.makeText(getActivity(), getString(R.string.not_complete_change_password), Toast.LENGTH_SHORT).show();

                                        } catch (Exception ex) {

                                            ex.printStackTrace();
                                        }

                                        Log.d(TAG, "Error auth failed");
                                        mInputPassword.setError(getResources().getString(R.string.password_current_incorrect));
                                        mInputPassword.requestFocus();
                                    }
                                });
                    }
                }
            }
        }
    }

    private void updateUI() {

        if (mAuth != null) {

            mAuth.signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private void forgotPassword() {

        Intent intent = new Intent(getContext(), ForgotPasswordActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;

    }
}
