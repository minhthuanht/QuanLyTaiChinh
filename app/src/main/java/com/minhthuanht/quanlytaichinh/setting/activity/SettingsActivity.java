package com.minhthuanht.quanlytaichinh.setting.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.minhthuanht.quanlytaichinh.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SharedPreferences mSharedPreferences;

        private SwitchPreferenceCompat mSync;

        private SwitchPreferenceCompat mNotification;

        private SwitchPreferenceCompat mSendToEmail;

        private final SwitchPreferenceCompat.OnPreferenceChangeListener mSyncChanged = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (mSync.isChecked()) {
                    syncDatabaseToServer();
                } else if (!mSync.isChecked()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.start_sync), Toast.LENGTH_SHORT).show();
                    mSync.setChecked(true);
                }
                return false;
            }
        };

        private final SwitchPreferenceCompat.OnPreferenceChangeListener mNotificationChanged = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (mNotification.isChecked()) {
                    turnOffNotification();
                } else if (!mNotification.isChecked()) {
                    turnOnNotification();
                }
                return false;
            }
        };


        private final SwitchPreferenceCompat.OnPreferenceChangeListener mSendToEmailChanged = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (mSendToEmail.isChecked()) {
                    turnOffSendToEmail();
                } else if (!mSendToEmail.isChecked()) {
                    turnOnSendToEmail();
                }
                return false;
            }
        };


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            mSharedPreferences = getContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            mSync = findPreference(getResources().getString(R.string.sync));
            mSync.setChecked(true);
            mNotification = findPreference(getResources().getString(R.string.notifi));
            mSendToEmail = findPreference(getResources().getString(R.string.send_to_email));

            initEvents();

        }

        private void initEvents() {
            mSync.setOnPreferenceChangeListener(mSyncChanged);
            mNotification.setOnPreferenceChangeListener(mNotificationChanged);
            mSendToEmail.setOnPreferenceChangeListener(mSendToEmailChanged);
        }

        private void turnOffNotification() {
            mNotification.setChecked(false);
            mSendToEmail.setChecked(false);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean("notification",mNotification.isChecked());
            editor.apply();
        }

        private void turnOnNotification() {
            mNotification.setChecked(true);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean("notification",mNotification.isChecked());
            editor.apply();

        }

        private void turnOnSendToEmail() {
            mSendToEmail.setChecked(true);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean("send_to_email",mNotification.isChecked());
            editor.apply();

        }

        private void turnOffSendToEmail() {
            mSendToEmail.setChecked(false);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean("send_to_email",mNotification.isChecked());
            editor.apply();
        }

        private void syncDatabaseToServer() {


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(getResources().getString(R.string.title_activity_settings));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }


}