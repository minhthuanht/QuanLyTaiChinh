package com.minhthuanht.quanlytaichinh.overviewtransaction;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.view.CurrencyTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class OVTransactionMonth extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "OVTransactionMonth";

    private static final String ARG_ITEMS = "items";

    // TODO: Rename and change types of parameters

    private CurrencyTextView mMoneyIncome;

    private CurrencyTextView mMoneyExpenses;

    private CurrencyTextView mMoneyIncomeNet;

    private ImageView mImageExMax;

    private TextView mLabelExMax;

    private TextView mNoteExMax;

    private TextView mTradingExMax;

    private ImageView mImageExMin;

    private TextView mLabelExMin;

    private TextView mNoteExMin;

    private TextView mTradingExMin;

    private ImageView mImageInMax;

    private TextView mLabelInMax;

    private TextView mNoteInMax;

    private TextView mTradingInMax;

    private ImageView mImageInMin;

    private TextView mLabelInMin;

    private TextView mNoteInMin;

    private TextView mTradingInMin;

    private PieChart mPiechartEx;

    private PieChart mPiechartIn;

    private RelativeLayout mLayout1;

    private RelativeLayout mLayout2;

    private RelativeLayout mLayout3;

    private RelativeLayout mLayout4;

    private List<Transaction> mItems = new ArrayList<>();


    public OVTransactionMonth() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OVTransactionMonth newInstance(List<Transaction> transactions) {
        OVTransactionMonth fragment = new OVTransactionMonth();
        Bundle args = new Bundle();
        assert transactions != null;
        args.putParcelableArrayList(ARG_ITEMS, (ArrayList<? extends Parcelable>) transactions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mItems = getArguments().getParcelableArrayList(ARG_ITEMS);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ov_transaction_month, container, false);

        mMoneyIncome = view.findViewById(R.id.txtIncome);
        mMoneyExpenses = view.findViewById(R.id.txtExpenses);
        mMoneyIncomeNet = view.findViewById(R.id.txtIncomeNet);

        mImageExMax = view.findViewById(R.id.imgItemExMax);
        mLabelExMax = view.findViewById(R.id.txtLableExMax);
        mNoteExMax = view.findViewById(R.id.txtNoteExMax);
        mTradingExMax = view.findViewById(R.id.txtTrandingExMax);

        mImageExMin = view.findViewById(R.id.imgItemExMin);
        mLabelExMin = view.findViewById(R.id.txtLableExMin);
        mNoteExMin = view.findViewById(R.id.txtNoteExMin);
        mTradingExMin = view.findViewById(R.id.txtTrandingExMin);

        mImageInMax = view.findViewById(R.id.imgItemInMax);
        mLabelInMax = view.findViewById(R.id.txtLableInMax);
        mNoteInMax = view.findViewById(R.id.txtNoteInMax);
        mTradingInMax = view.findViewById(R.id.txtTrandingInMax);

        mImageInMin = view.findViewById(R.id.imgItemInMin);
        mLabelInMin = view.findViewById(R.id.txtLableInMin);
        mNoteInMin = view.findViewById(R.id.txtNoteInMin);
        mTradingInMin = view.findViewById(R.id.txtTrandingInMin);

        mPiechartEx = view.findViewById(R.id.piechartExpenses);
        mPiechartIn = view.findViewById(R.id.piechartIncome);
        mLayout1 = view.findViewById(R.id.layout_ov1);
        mLayout2 = view.findViewById(R.id.layout_ov2);
        mLayout3 = view.findViewById(R.id.layout_ov3);
        mLayout4 = view.findViewById(R.id.layout_ov4);

        onBindView();

        initEvents();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.over_view_month));
    }

    private void initEvents() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onBindView() {

        float moneyEx = 0;
        float moneyIn = 0;
        List<Transaction> itemsEx = new ArrayList<>();
        List<Transaction> itemsIn = new ArrayList<>();


        for (Transaction tran : mItems) {

            if (tran.getMoneyTradingWithSign() < 0) {

                moneyEx += Math.abs(tran.getMoneyTradingWithSign());
                itemsEx.add(tran);

            } else {

                moneyIn += Math.abs(tran.getMoneyTradingWithSign());
                itemsIn.add(tran);
            }
        }

        float moneyIncomeNet = moneyIn - moneyEx;

        mMoneyIncome.setText(String.valueOf(moneyIn));
        mMoneyIncome.setTextColor(getResources().getColor(R.color.colorMoneyTradingPositive));

        mMoneyExpenses.setText(String.valueOf(moneyEx));
        mMoneyExpenses.setTextColor(getResources().getColor(R.color.colorMoneyTradingNegative));

        mMoneyIncomeNet.setText(String.valueOf(moneyIncomeNet));
        if (moneyIncomeNet >= 0) {

            mMoneyIncomeNet.setTextColor(getResources().getColor(R.color.colorMoneyTradingPositive));
        } else {

            mMoneyIncomeNet.setTextColor(getResources().getColor(R.color.colorMoneyTradingNegative));
        }

        if (!itemsEx.isEmpty()) {

            onBindViewExMax(itemsEx);
            onBindViewExMin(itemsEx);
        } else {

            mLayout1.setVisibility(View.GONE);
            mLayout3.setVisibility(View.GONE);
        }

        if (!itemsIn.isEmpty()) {

            onBindViewInMax(itemsIn);
            onBindViewInMin(itemsIn);
        } else {

            mLayout2.setVisibility(View.GONE);
            mLayout4.setVisibility(View.GONE);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onBindViewExMax(final List<Transaction> itemsEx) {

        // lấy item có properties trading lớn nhất
        Transaction transaction = Collections.max(itemsEx, Comparator.comparing(Transaction::getTransactionTrading));
        mLabelExMax.setText(transaction.getTransactionCategoryID().getCategory());
        mNoteExMax.setText(transaction.getTransactionNote());
        mTradingExMax.setText(String.valueOf((int) transaction.getTransactionTrading()));
        mTradingExMax.setTextColor(getResources().getColor(R.color.colorMoneyTradingNegative));

        // lấy icon
        String path_icon = "category/";

        try {
            Drawable drawable = Drawable.createFromStream(Objects.requireNonNull(getActivity()).getAssets()
                    .open(path_icon + transaction.getTransactionCategoryID().getCategoryIcon()), null);

            mImageExMax.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onBindViewExMin(List<Transaction> itemsEx) {

        Transaction transaction = Collections.min(itemsEx, Comparator.comparing(Transaction::getTransactionTrading));
        mLabelExMin.setText(transaction.getTransactionCategoryID().getCategory());
        mNoteExMin.setText(transaction.getTransactionNote());
        mTradingExMin.setText(String.valueOf((int) transaction.getTransactionTrading()));
        mTradingExMin.setTextColor(getResources().getColor(R.color.colorMoneyTradingNegative));

        // lấy icon
        String path_icon = "category/";

        try {
            Drawable drawable = Drawable.createFromStream(Objects.requireNonNull(getActivity()).getAssets()
                    .open(path_icon + transaction.getTransactionCategoryID().getCategoryIcon()), null);

            mImageExMin.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onBindViewInMax(List<Transaction> itemsIn) {

        Transaction transaction = Collections.max(itemsIn, Comparator.comparing(Transaction::getTransactionTrading));
        mLabelInMax.setText(transaction.getTransactionCategoryID().getCategory());
        mNoteInMax.setText(transaction.getTransactionNote());
        mTradingInMax.setText(String.valueOf((int) transaction.getTransactionTrading()));
        mTradingInMax.setTextColor(getResources().getColor(R.color.colorMoneyTradingPositive));


        // lấy icon
        String path_icon = "category/";

        try {
            Drawable drawable = Drawable.createFromStream(Objects.requireNonNull(getActivity()).getAssets()
                    .open(path_icon + transaction.getTransactionCategoryID().getCategoryIcon()), null);

            mImageInMax.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onBindViewInMin(List<Transaction> itemsIn) {

        Transaction transaction = Collections.min(itemsIn, Comparator.comparing(Transaction::getTransactionTrading));
        mLabelInMin.setText(transaction.getTransactionCategoryID().getCategory());
        mNoteInMin.setText(transaction.getTransactionNote());
        mTradingInMin.setText(String.valueOf((int) transaction.getTransactionTrading()));
        mTradingInMin.setTextColor(getResources().getColor(R.color.colorMoneyTradingPositive));

        // lấy icon
        String path_icon = "category/";

        try {
            Drawable drawable = Drawable.createFromStream(Objects.requireNonNull(getActivity()).getAssets()
                    .open(path_icon + transaction.getTransactionCategoryID().getCategoryIcon()), null);

            mImageInMin.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
