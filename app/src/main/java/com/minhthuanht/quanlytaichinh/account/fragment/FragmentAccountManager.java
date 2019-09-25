package com.minhthuanht.quanlytaichinh.account.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.WelcomeActivity;

import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FragmentAccountManager extends Fragment {

    private TextView mFirstName;

    private TextView mNameEmail;

    private TextView mEmail;

    private Button mUpgrade;

    private Button mChangePassword;

    private Button mLogout;

    private FirebaseAuth mAuth;

    private final View.OnClickListener mUpgradeListener = view -> Toast.makeText(getContext(), getString(R.string.feature_developing), Toast.LENGTH_SHORT).show();

    private final View.OnClickListener mChangePassListener = view -> changePassword();

    private final View.OnClickListener mLogoutListener = view -> logOutAccount();

    public FragmentAccountManager() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_account_manager, container, false);
        mFirstName = view.findViewById(R.id.txtFirstName);
        mNameEmail = view.findViewById(R.id.txtNameEmail);
        mEmail = view.findViewById(R.id.txtEmail);
        mUpgrade = view.findViewById(R.id.btnUpgrade);
        mChangePassword = view.findViewById(R.id.btnChangePassWord);
        mLogout = view.findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();

        onBindView();

        initEvents();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.information_account));
    }

    private void initEvents() {

        mUpgrade.setOnClickListener(mUpgradeListener);
        mChangePassword.setOnClickListener(mChangePassListener);
        mLogout.setOnClickListener(mLogoutListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void onBindView() {

        if (mAuth != null) {

            String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

            if (email != null) {

                String firstCharAt = String.valueOf(email.charAt(0));
                mFirstName.setText(firstCharAt);

                String[] mail = email.split("@");
                String name = mail[0];
                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                mNameEmail.setText(name);

                mEmail.setText(email);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void changePassword() {

        Fragment fragment = new FragmentChangePassword();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, fragment)
                .addToBackStack(null).commit();
    }


    private void logOutAccount() {

        if (mAuth != null) {

            mAuth.signOut();
            Intent intent = new Intent(getContext(), WelcomeActivity.class);
            startActivity(intent);
        }
    }

}
