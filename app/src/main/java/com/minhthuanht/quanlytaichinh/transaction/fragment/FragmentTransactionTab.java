package com.minhthuanht.quanlytaichinh.transaction.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.astuetz.PagerSlidingTabStrip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.transaction.adapter.TransactionPagerAdapter;
import com.minhthuanht.quanlytaichinh.implementDAO.ITransactionsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.IWalletsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.TransactionsDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.WalletsDAOimpl;
import com.minhthuanht.quanlytaichinh.model.DateRange;
import com.minhthuanht.quanlytaichinh.model.MTDate;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.model.Wallet;
import com.minhthuanht.quanlytaichinh.transaction.activity.AddTransactionActivity;
import com.minhthuanht.quanlytaichinh.utilities.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FragmentTransactionTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private static final String TAG = "FragmentTransactionTab";
    private static final int ADD_TRANSACTION_REQUEST_CODE = 0;

    private FirebaseAuth mAuth;

    private FloatingActionButton mAddTransaction;

    private ViewPager mViewPager;

    private TransactionPagerAdapter mTransactionPagerAdapter;

    private List<Pair<String, Fragment>> mTabFragment;

    private List<Transaction> mListTransaction;

    private ITransactionsDAO mITransactionsDAO;

    private Wallet mCurrentWallet = null;

//    private Dialog mDialog;

    private DateUtils mDateUtils = new DateUtils();

    private final View.OnClickListener mAddTransactionListener = view -> {

        Intent intent = new Intent(getContext(), AddTransactionActivity.class);
        startActivityForResult(intent, ADD_TRANSACTION_REQUEST_CODE);
    };

    private final ViewPager.OnPageChangeListener mViewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            mTransactionPagerAdapter.getItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public FragmentTransactionTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_tab, container, false);
        PagerSlidingTabStrip mTabLayout = view.findViewById(R.id.tab_layout);
        mViewPager = view.findViewById(R.id.viewPager);
        mAddTransaction = view.findViewById(R.id.addTransaction);


//        DateUtils mDateUtils = new DateUtils();
        mTabFragment = new ArrayList<>();

        IWalletsDAO mIWalletsDAO = new WalletsDAOimpl(getContext());
        if (mAuth.getCurrentUser() != null) {

            mCurrentWallet = mIWalletsDAO.getAllWalletByUser(mAuth.getCurrentUser().getUid()).get(0);
        }

        mITransactionsDAO = new TransactionsDAOimpl(getContext());
        if (mCurrentWallet != null) {

            mListTransaction = mITransactionsDAO.getAllTransactionByWalletId(mCurrentWallet.getWalletID());
        }

        mTransactionPagerAdapter = new TransactionPagerAdapter(getChildFragmentManager(), mTabFragment);
        mViewPager.setAdapter(mTransactionPagerAdapter);
        mTabLayout.setViewPager(mViewPager);

        initEvents();

        return view;
    }

    private void initEvents() {

        mAddTransaction.setOnClickListener(mAddTransactionListener);
        mViewPager.addOnPageChangeListener(mViewPagerListener);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        new LoadTabs(getActivity()).execute();
        super.onResume();

        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.transaction_book));
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(FragmentTransactionTab.class.getSimpleName(), "On Activity Result");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TRANSACTION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getBundleExtra(getResources().getString(R.string.complete));
                assert bundle != null;
                Transaction transaction = bundle.getParcelable(getResources().getString(R.string.add_transaction_complete));
                mListTransaction.add(transaction);

            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class LoadTabs extends AsyncTask<Void, Void, Void> {

        private Activity mActivity;


        LoadTabs(Activity activity) {

            if (activity != null) {

                mActivity = activity;
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

//            mDialog = new Dialog(mActivity);
//            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            mDialog.setContentView(R.layout.loading_view);
//            mListTransaction.clear();
//            mDialog.setCancelable(false);
//            mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//            mDialog.dismiss();
            scrollToCurrentMonth();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            Log.d(TAG, "ddddđ");
            mListTransaction = mITransactionsDAO.getAllTransactionByWalletId(mCurrentWallet.getWalletID());

            // su dung phuong thuc de update lai adapter
            mActivity.runOnUiThread(runnableUpdateAdapter);
            return (null);
        }
    }


    private Runnable runnableUpdateAdapter = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {

            // thuc hien update lai adapter
            mTabFragment.clear();
            DateRange dateRange = new DateRange(new MTDate(2018, 0, 1).firstDayOfMonth().setTimeToBeginningOfDay(), new MTDate());
            addTabs(dateRange);
            mTransactionPagerAdapter.updateViewPager(mTabFragment);

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addTabs(DateRange dateRange) {

        int year, month = 0;
        int from_month = dateRange.getDateFrom().getMonth();
        int from_year = dateRange.getDateFrom().getYear();
        int to_month = dateRange.getDateTo().getMonth();
        int to_year = dateRange.getDateTo().getYear();

        for (year = from_year; year <= to_year; ++year) {
            for (month = 0; month < 12; ++month) {
                if (year == from_year && month < from_month) continue;
                if (year == to_year && month > to_month) break;
                MTDate firstDay = new MTDate(year, month, 1).firstDayOfMonth().setTimeToBeginningOfDay();
                MTDate lastDay = new MTDate(year, month, 1).lastDayOfMonth().setTimeToEndOfDay();
                DateRange period = new DateRange(firstDay, lastDay);
                addTab(period);
            }
        }
        if (month == 12) {
            month = 0;
            ++year;
        }
        MTDate firstFuture = new MTDate(year, month, 1).firstDayOfMonth();
        MTDate lastFuture = new MTDate(year + 100, month, 1).lastDayOfMonth();
        DateRange period = new DateRange(firstFuture, lastFuture);
        addTab(period);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("DefaultLocale")
    private void addTab(DateRange dateRange) {

        MTDate mtDate = dateRange.getDateFrom();
        int month = mtDate.getMonth();
        int year = mtDate.getYear();
        MTDate current = new MTDate();
        int current_month = current.getMonth();
        int current_year = current.getYear();

        String title;

        if (year < current_year) {

            title = String.format("%d/%d", month + 1, year);
        } else if (year == current_year && month == current_month) {

            title = getResources().getString(R.string.current_month);
        } else if (year == current_year && month < current_month) {

            title = String.format("%d/%d", month + 1, year);
        } else {

            title = getResources().getString(R.string.future_month);
        }

        List<Transaction> transactions = filterTransactions(dateRange, mListTransaction);

//        Fragment fragment = test.newInstance(transactions);
        Fragment fragment = FragmentListTransaction.newInstance(transactions);
        mTabFragment.add(new Pair<>(title, fragment));
    }

    private void scrollToCurrentMonth() {

        for (int i = mTabFragment.size() - 1; i >= 0; i--) {

            if (mTabFragment.get(i).first.compareTo(getResources().getString(R.string.current_month)) == 0) {

//                mTabLayout.setScrollPosition(i, 0.0f, true);
                mViewPager.setCurrentItem(i);
            }
        }
    }

    private List<Transaction> filterTransactions(DateRange dateRange, List<Transaction> transactions) {
        List<Transaction> filter = new ArrayList<>();
        for (Transaction t : transactions) {
            if (mDateUtils.isDateRangeContainDate(dateRange, t.getTransactionDate())) {
                filter.add(t);
            }
        }
        return filter;
    }
}
