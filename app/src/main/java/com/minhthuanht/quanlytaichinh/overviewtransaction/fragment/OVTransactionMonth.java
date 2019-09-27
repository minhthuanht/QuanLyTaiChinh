package com.minhthuanht.quanlytaichinh.overviewtransaction.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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


    private List<Float> mYDataEx = new ArrayList<>();

    private List<String> mXDataEx = new ArrayList<>();

    private List<Float> mYDataIn = new ArrayList<>();

    private List<String> mXDataIn = new ArrayList<>();

    private final OnChartValueSelectedListener mPiechartExListener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {

//            showItemSelect(e, h, mYDataEx, mXDataEx);


        }

        @Override
        public void onNothingSelected() {

        }
    };

    private final OnChartValueSelectedListener mPiechartInListener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {

//            showItemSelect(e, h, mYDataIn, mXDataIn);
        }

        @Override
        public void onNothingSelected() {

        }
    };

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

        mPiechartEx.setRotationEnabled(true);
        mPiechartEx.getDescription().setText("Biểu đồ thống kê giao dịch");
        mPiechartEx.setHoleRadius(35f);
        mPiechartEx.setTransparentCircleAlpha(0);
        mPiechartEx.setCenterText("Khoản chi");
        mPiechartEx.setCenterTextSize(10);
        mPiechartEx.setDrawEntryLabels(true);

        mPiechartEx.setDrawSliceText(false); // To remove slice text
        mPiechartEx.setDrawMarkers(false); // To remove markers when click
        mPiechartEx.setDrawEntryLabels(false); // To remove labels from piece of pie
        mPiechartEx.getDescription().setEnabled(false);

        mPiechartIn = view.findViewById(R.id.piechartIncome);
        mPiechartEx.getDescription().setText("Biểu đồ thống kê giao dịch");
        ;
        mPiechartIn.setHoleRadius(35f);
        mPiechartIn.setTransparentCircleAlpha(0);
        mPiechartIn.setCenterText("Khoản thu");
        mPiechartIn.setCenterTextSize(10);
        mPiechartIn.setDrawEntryLabels(true);

        mPiechartIn.setDrawSliceText(false); // To remove slice text
        mPiechartIn.setDrawMarkers(false); // To remove markers when click
        mPiechartIn.setDrawEntryLabels(false); // To remove labels from piece of pie
        mPiechartIn.getDescription().setEnabled(false);


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
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.over_view_month));
    }

    private void initEvents() {

        mPiechartEx.setOnChartValueSelectedListener(mPiechartExListener);
        mPiechartIn.setOnChartValueSelectedListener(mPiechartInListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onBindView() {

        float moneyEx = 0;
        float moneyIn = 0;


        float money1 = 0, money5 = 0, money13 = 0, money18 = 0,
                money23 = 0, money24 = 0, money27 = 0, money28 = 0,
                money33 = 0, money37 = 0, money42 = 0, money49 = 0, money51 = 0,
                money53 = 0, money56 = 0;


        List<Transaction> itemsEx = new ArrayList<>();
        List<Transaction> itemsIn = new ArrayList<>();

        for (Transaction tran : mItems) {

            if (tran.getTransactionCategoryID().getCategoryParentId() == 1) {

                money1 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 5) {

                money5 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 13) {

                money13 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 18) {

                money18 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 23) {

                money23 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 24) {

                money24 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 27) {

                money27 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 28) {

                money28 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 33) {

                money33 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 37) {

                money37 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 42) {

                money42 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 49) {

                money49 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 51) {

                money51 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 53) {

                money53 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getTransactionCategoryID().getCategoryParentId() == 56) {

                money56 += Math.abs(tran.getMoneyTradingWithSign());
            }

            if (tran.getMoneyTradingWithSign() < 0) {

                moneyEx += Math.abs(tran.getMoneyTradingWithSign());
                itemsEx.add(tran);

            }

            if (tran.getMoneyTradingWithSign() >= 0) {

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

        List<Pair<Float, String>> totalCategory = new ArrayList<>();

        totalCategory.add(new Pair<>(money1, "Ăn uống"));
        totalCategory.add(new Pair<>(money5, "Hóa đơn & Tiện ích"));
        totalCategory.add(new Pair<>(money13, "Di chuyển"));
        totalCategory.add(new Pair<>(money18, "Mua sắm"));
        totalCategory.add(new Pair<>(money23, "Bạn bè & Người yêu"));
        totalCategory.add(new Pair<>(money24, "Giải trí"));
        totalCategory.add(new Pair<>(money27, "Du lịch"));
        totalCategory.add(new Pair<>(money28, "Sức khỏe"));
        totalCategory.add(new Pair<>(money33, "Quà tặng & Khuyên góp"));
        totalCategory.add(new Pair<>(money37, "Gia đình"));
        totalCategory.add(new Pair<>(money42, "Giáo dục"));
        totalCategory.add(new Pair<>(money49, "Khoản chi khác"));
        totalCategory.add(new Pair<>(money51, "Thưởng"));
        totalCategory.add(new Pair<>(money53, "Lương"));
        totalCategory.add(new Pair<>(money56, "Khoản thu khác"));

        List<Pair<Float, String>> totalDataEx = new ArrayList<>();
        List<Pair<Float, String>> totalDataIn = new ArrayList<>();

        for (int i = 0; i < totalCategory.size(); i++) {

            if (totalCategory.get(i).first > 0) {

                if (totalCategory.get(i).first == money51 ||totalCategory.get(i).first == money53 || totalCategory.get(i).first == money56) {

                    totalDataIn.add(new Pair<>(totalCategory.get(i).first, totalCategory.get(i).second));
                    mYDataIn.add(totalCategory.get(i).first);
                    mXDataIn.add(totalCategory.get(i).second);

                } else {

                    totalDataEx.add(new Pair<>(totalCategory.get(i).first, totalCategory.get(i).second));
                    mYDataEx.add(totalCategory.get(i).first);
                    mXDataEx.add(totalCategory.get(i).second);

                }
            }
        }
        addDataSet(mPiechartEx, totalDataEx);
        addDataSet(mPiechartIn, totalDataIn);
    }

    private void addDataSet(PieChart pieChart, List<Pair<Float, String>> totalData) {

        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();

        for (int i = 0; i < totalData.size(); i++) {

            yEntries.add(new PieEntry(totalData.get(i).first, i));
            xEntries.add(totalData.get(i).second);
        }


        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntries, "TransactionChart");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        colors.add(Color.BLACK);
        colors.add(R.color.o_300);
        colors.add(Color.LTGRAY);
        colors.add(R.color.o_900);
        colors.add(Color.DKGRAY);


        pieDataSet.setColors(colors);

        List<LegendEntry> entries = new ArrayList<>();

        for (int i = 0; i < xEntries.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors.get(i);
            entry.label = xEntries.get(i);
            entries.add(entry);
        }

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        legend.setCustom(entries);

        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER); // set vertical alignment for legend
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // set horizontal alignment for legend
        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // set orientation for legend
        legend.setDrawInside(false);


        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
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


//    private void showItemSelect(Entry e, Highlight h, List<Float> y, List<String> x) {
//
//        int pos1 = e.toString().indexOf(":");
//        String moneyTranding = e.toString().substring(pos1 + 9);
//
//        for (int i = 0; i < y.size(); i++) {
//            if (y.get(i) == Float.parseFloat(moneyTranding)) {
//                pos1 = i;
//                break;
//            }
//        }
//        String nameCategory = x.get(pos1);
//        Toast.makeText(getContext(), nameCategory + ":" + moneyTranding + "đ", Toast.LENGTH_LONG).show();
//
//    }

}
