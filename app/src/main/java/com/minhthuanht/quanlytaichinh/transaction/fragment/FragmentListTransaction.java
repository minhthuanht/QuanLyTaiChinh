package com.minhthuanht.quanlytaichinh.transaction.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.adapter.TransactionListAdapter;
import com.minhthuanht.quanlytaichinh.model.MTDate;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.overviewtransaction.OVTransactionMonth;
import com.minhthuanht.quanlytaichinh.pinnedheaderlistview.PinnedHeaderListView;
import com.minhthuanht.quanlytaichinh.transaction.activity.DetailTransactionActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FragmentListTransaction extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "FragmentListTransaction";

    private static final String BUNDLE_LIST_ITEM = "TransactionListFragment.bundle.list_items";

    private static final int REQUEST_VIEW_TRANSACTION = 100;

    private PinnedHeaderListView mLViewTransaction;

    private TransactionListAdapter mAdapter;

    private LinearLayout mBlankLayout;

    private List<Transaction> mItems = new ArrayList<>();

    private List<Pair<Date, List<Transaction>>> mFilterItems;

    private View headerView;

    private final PinnedHeaderListView.OnItemClickListener mLViewTransactionListener = new PinnedHeaderListView.OnItemClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {

            Transaction transaction = (Transaction) mAdapter.getItem(section, position);
            onClickItem(transaction);
        }

        @Override
        public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {

        }

        @Override
        public void onHeaderClick(AdapterView<?> adapterView, View view, int header, long id) {

        }
    };


    public FragmentListTransaction() {
        // Required empty public constructor
    }

    static FragmentListTransaction newInstance(List<Transaction> transactions) {
        FragmentListTransaction fragment = new FragmentListTransaction();
        Bundle args = new Bundle();
        Log.d(TAG, String.valueOf(transactions.size()));
        args.putParcelableArrayList(BUNDLE_LIST_ITEM, (ArrayList<? extends Parcelable>) transactions);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            mItems = getArguments().getParcelableArrayList(BUNDLE_LIST_ITEM);
        }

        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_list_transaction, container, false);
        mLViewTransaction = view.findViewById(R.id.list_view_transaction);
        mBlankLayout = view.findViewById(R.id.layout_transaction_empty);
        mFilterItems = new ArrayList<>();
        mAdapter = new TransactionListAdapter(mFilterItems);
        mLViewTransaction.setAdapter(mAdapter);

        initEvents();

        new loadTransactions(this.getActivity()).execute();

        return view;
    }

    private void initEvents() {

        mLViewTransaction.setOnItemClickListener(mLViewTransactionListener);

    }

    @Override
    public void onResume() {
        super.onResume();
//        new loadTransactions(this.getActivity()).execute();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

    }

    @SuppressLint("StaticFieldLeak")
    private class loadTransactions extends AsyncTask<Void, Void, Void> {

        private Activity mActivity;

        loadTransactions(FragmentActivity activity) {

            if (activity != null) {

                mActivity = activity;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mActivity.runOnUiThread(runnableUpdateAdapter);
            return (null);
        }
    }

    private Runnable runnableUpdateAdapter = this::updateUI;

    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateUI() {

        if (mItems.size() == 0) {
            mLViewTransaction.setVisibility(View.INVISIBLE);
            mLViewTransaction.removeHeaderView(headerView);
            mBlankLayout.setVisibility(View.VISIBLE);
        } else {

            mBlankLayout.setVisibility(View.INVISIBLE);

            if (headerView == null) {

                LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                assert inflater != null;
                headerView = inflater.inflate(R.layout.header_transaction_statistics, null, false);
            }

            mLViewTransaction.addHeaderView(headerView);
            updateHeaderView();
            // update lai adapter
            mFilterItems.clear();
            filterPairTransactions(mItems);
            mAdapter.updateValues(mFilterItems);
            mAdapter.notifyDataSetChanged();
            mLViewTransaction.setVisibility(View.VISIBLE);
        }

    }

    private void filterPairTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            filterPairTransaction(transaction);
        }
    }

    private void filterPairTransaction(Transaction transaction) {
        int index = -1;
        MTDate date = new MTDate(transaction.getTransactionDate());
        for (int i = 0; i < mFilterItems.size(); ++i) {
            MTDate dateI = new MTDate(mFilterItems.get(i).first);
            if (compareDate(date, dateI) == 0) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            MTDate dateI = new MTDate(mFilterItems.get(index).first);
            if (compareDate(date, dateI) == 0) {
                mFilterItems.get(index).second.add(0, transaction);
            }
        } else {
            index = 0;
            for (int i = 0; i < mFilterItems.size(); ++i) {
                MTDate dateI = new MTDate(mFilterItems.get(i).first);
                if (compareDate(date, dateI) > 0) {
                    index = i;
                    break;
                }
            }
            ArrayList<Transaction> trans = new ArrayList<>();
            trans.add(transaction);
            mFilterItems.add(index, new Pair<>(transaction.getTransactionDate(), trans));
        }
    }

    private int compareDate(MTDate date1, MTDate date2) {
        return date1.setTimeToBeginningOfDay().getCalendar().compareTo(date2.setTimeToBeginningOfDay().getCalendar());

    }

    private void updateHeaderView() {

        float tienChi = 0;
        float tienTieu = 0;

        if (headerView != null) {

            for (Transaction tran : mItems) {

                if (tran.getMoneyTradingWithSign() < 0) {

                    tienTieu += Math.abs(tran.getMoneyTradingWithSign());
                } else {

                    tienChi += Math.abs(tran.getMoneyTradingWithSign());
                }
            }

            TextView textChi = headerView.findViewById(R.id.fts_so_du_dau);
            TextView textTieu = headerView.findViewById(R.id.fts_so_du_cuoi);
            TextView textConLai = headerView.findViewById(R.id.fts_con_lai);
            Button buttonReport = headerView.findViewById(R.id.btnReport);

            buttonReport.setOnClickListener(view -> reportTransaction(mItems));
            Log.d(TAG, "aaaaaa");

            String moneyChi = String.valueOf(tienChi);//CurrencyUtils.formatVnCurrence(String.format(Constants.PRICE_FORMAT,tienChi));
            String moneyTieu = String.valueOf(tienTieu);// CurrencyUtils.formatVnCurrence(String.format(Constants.PRICE_FORMAT,tienTieu));
            String moneyConLai = String.valueOf(tienChi - tienTieu);//CurrencyUtils.formatVnCurrence(String.format(Constants.PRICE_FORMAT,tienChi - tienTieu));
            textChi.setText(moneyChi);
            textTieu.setText(moneyTieu);
            textConLai.setText(moneyConLai);


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void reportTransaction(List<Transaction> mItems) {

        Fragment fragment = OVTransactionMonth.newInstance(mItems);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame_layout, fragment)
                .addToBackStack(null).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void onClickItem(Transaction transaction) {

        Intent intent = new Intent(getContext(), DetailTransactionActivity.class);

        intent.putExtra("transactionItem", transaction);
        startActivityForResult(intent, REQUEST_VIEW_TRANSACTION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
