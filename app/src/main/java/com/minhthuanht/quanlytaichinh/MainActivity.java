package com.minhthuanht.quanlytaichinh;

import android.content.Intent;
import android.os.Bundle;


import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.minhthuanht.quanlytaichinh.account.fragment.FragmentAccountManager;
import com.minhthuanht.quanlytaichinh.overviewtransaction.fragment.FragmentTendency;
import com.minhthuanht.quanlytaichinh.transaction.fragment.FragmentTransactionTab;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_transaction));
        TextView mEmailCurrent = navigationView.getHeaderView(0).findViewById(R.id.txtEmailCurent);
        TextView mNameUser = navigationView.getHeaderView(0).findViewById(R.id.txtNameCurrent);

        if (mAuth.getCurrentUser() != null) {

            String emailCurrent = mAuth.getCurrentUser().getEmail();
            assert emailCurrent != null;
            String[] arr = emailCurrent.split("@");
            String nameCurrent = arr[0];

            mEmailCurrent.setText(emailCurrent);
            mNameUser.setText(nameCurrent);
        }

    }

    public void onBackPressed() {
        DrawerLayout layout = findViewById(R.id.drawer_layout);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_transaction) {
            // Handle

            fragmentClass = FragmentTransactionTab.class;

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            assert fragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, fragment).commit();
            setTitle(item.getTitle());

        } else {
            if (id == R.id.nav_chart) {
                // Handle
                fragmentClass = FragmentTendency.class;

            } else if (id == R.id.nav_paybook){
                // handle
            }

            else if (id == R.id.nav_manage) {
                // Handle
                Toast.makeText(this, "not handle", Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_budget) {
                // Handle
                Toast.makeText(this, "not handle", Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_share) {
                // Handle
                Toast.makeText(this, "not handle", Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_send) {
                // Handle
                Toast.makeText(this, "not handle", Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_account) {

                if (mAuth != null) {

                    fragmentClass = FragmentAccountManager.class;
                }

            } else if (id == R.id.nav_logout) {

                if (mAuth != null) {

                    mAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }
            }

            if (fragmentClass != null) {

                try {

                    fragment = (Fragment) fragmentClass.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, fragment).addToBackStack(null).commit();
                    setTitle(item.getTitle());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
