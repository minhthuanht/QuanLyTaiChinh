package com.minhthuanht.quanlytaichinh.budget.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.implementDAO.BudgetDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.IBudgetDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.ITransactionsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.TransactionsDAOimpl;
import com.minhthuanht.quanlytaichinh.model.Budget;
import com.minhthuanht.quanlytaichinh.model.DateRange;
import com.minhthuanht.quanlytaichinh.model.MTDate;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.view.CurrencyTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailBudgetActivity extends AppCompatActivity {

    private static final String REQUEST_DETAIL_BUDGET = "sendDetailBudgetActivity";

    private static final String CODE_SEND_EDIT_BUDGET = "sendEditBudgetActivity";

    private static final int REQUEST_EDIT_BUDGET = 222;

    private Budget mBudget = new Budget();

    private IBudgetDAO mIBudgetDAO;

    private ImageView mImgCategory;

    private TextView mCategoryBudget;

    private CurrencyTextView mMoneyBuget;

    private CurrencyTextView mMoneyDebt;

    private TextView mTitleMoney;

    private CurrencyTextView mMoneyRemain;

    private ProgressBar mPrgBudget;

    private TextView mDateBudget;

    private ImageView mImgWallet;

    private TextView mBudgetWallet;

    private LineChart mLineChart;

    private CurrencyTextView mMoneyDay;

    private CurrencyTextView mMoneyExpected;

    private CurrencyTextView mMoneyCurrentDebt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_budget);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initData();
        initControls();
        initEvents();

    }

    private void initData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(REQUEST_DETAIL_BUDGET);
        if (bundle != null) {

            mBudget = bundle.getParcelable(REQUEST_DETAIL_BUDGET);

        }
    }

    private void initControls() {

        mImgCategory = findViewById(R.id.imgCategory);
        mCategoryBudget = findViewById(R.id.txtCategoryBudget);
        mMoneyBuget = findViewById(R.id.txtMoneyBudget);
        mMoneyDebt = findViewById(R.id.txtMoneyDebt);
        mTitleMoney = findViewById(R.id.txtTitleMoney);
        mMoneyRemain = findViewById(R.id.txtMoneyRemain);
        mPrgBudget = findViewById(R.id.prgBudget);
        mDateBudget = findViewById(R.id.txtDateBudget);
        mImgWallet = findViewById(R.id.image_transaction_wallet);
        mBudgetWallet = findViewById(R.id.txtBudgetWallet);
        mLineChart = findViewById(R.id.lineChart);
        mMoneyDay = findViewById(R.id.txtMoneyDay);
        mMoneyExpected = findViewById(R.id.txtExpected);
        mMoneyCurrentDebt = findViewById(R.id.txtCurrentDebt);

        onBindView(mBudget);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_BUDGET) {

            if (resultCode == RESULT_OK) {

                Bundle bundle = data.getBundleExtra(EditBudgetActivity.RESULT_EDIT);
                mBudget = bundle.getParcelable(EditBudgetActivity.RESULT_EDIT);

                onBindView(mBudget);
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    private void onBindView(Budget budget) {

        if (budget != null) {


            String path_icon = "category/";
            try {
                Drawable drawable = Drawable.createFromStream(getAssets().open(path_icon + budget.getCategory().getCategoryIcon()), null);
                mImgCategory.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCategoryBudget.setText(budget.getCategory().getCategory());
            mMoneyBuget.setText(String.valueOf(budget.getAmount()));
            mMoneyDebt.setText(String.valueOf(budget.getSpent()));
            if (budget.getSpent() > budget.getAmount()) {

                mTitleMoney.setText(R.string.overspending);
            } else {
                mTitleMoney.setText(R.string.remain);
            }
            mMoneyRemain.setText(String.valueOf(Math.abs(budget.getAmount() - budget.getSpent())));
            mPrgBudget.setMax(100);
            int process = (int) (budget.getSpent() / budget.getAmount() * 100);
            mPrgBudget.setProgress(process);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mPrgBudget.setProgressTintList(ColorStateList.valueOf(R.color.r_900));
            }

            DateRange dateRange = new DateRange(budget.getTimeStart(), budget.getTimeEnd());
            mDateBudget.setText(dateRange.toString());

            try {
                Drawable drawable = Drawable.createFromStream(getAssets().open(path_icon + "icon.png"), null);
                mImgWallet.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mBudgetWallet.setText(this.mBudget.getWallet().getWalletName());

            // lineChart

            loadChartTransactions();
            //
            int remainDays = (int) Math.ceil((budget.getTimeEnd().getMillis() - budget.getTimeStart().getMillis()) / 24 / 60 / 60 / 1000f);
            mMoneyDay.setText(String.valueOf(budget.getAmount() / remainDays));

            MTDate mtDate = new MTDate();

            int remainDaysSpended = (int) Math.ceil((mtDate.getMillis() - budget.getTimeStart().getMillis()) / 24 / 60 / 60 / 1000f);
            if (remainDaysSpended == 0) {
                remainDaysSpended = 1;
            }

            mMoneyCurrentDebt.setText(String.valueOf(budget.getSpent() / remainDaysSpended));

            int remainDaysSpent = remainDays - remainDaysSpended;
            if (remainDaysSpent == 0) {

                remainDaysSpent = 1;
            }

            mMoneyExpected.setText(String.valueOf((budget.getAmount() - budget.getSpent()) / remainDaysSpent));

        }
    }

    private void initEvents() {

    }

    @Override
    public boolean onSupportNavigateUp() {

        setResult(RESULT_CANCELED);
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.meunu_transaction_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_delete) {

            deleteBudget();
        }

        if (item.getItemId() == R.id.action_edit) {

            editBudget();
        }

        return super.onOptionsItemSelected(item);
    }

    private void editBudget() {

        Intent intent = new Intent(this, EditBudgetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CODE_SEND_EDIT_BUDGET, mBudget);
        intent.putExtra(CODE_SEND_EDIT_BUDGET, bundle);
        startActivityForResult(intent, REQUEST_EDIT_BUDGET);

    }

    private void deleteBudget() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.title_delete_budget));
        builder.setMessage(getResources().getString(R.string.messing_delete));
        builder.setPositiveButton(getResources().getString(R.string.comfirm), (dialogInterface, i) -> {

            mIBudgetDAO = new BudgetDAOimpl(DetailBudgetActivity.this);
            mIBudgetDAO.deleteBudget(mBudget);

            setResult(RESULT_OK);
            finish();
        });

        builder.setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();

    }

    private void loadChartTransactions() {

        ITransactionsDAO iTransactionsDAO = new TransactionsDAOimpl(this);
        List<Transaction> transactions = iTransactionsDAO.getStatisticalByCategoryInRange(
                mBudget.getWallet().getWalletID(),
                mBudget.getCategory().getCategoryID(),
                new DateRange(mBudget.getTimeStart(), mBudget.getTimeEnd()));
        List<Entry> entries = filterAmountByDates(transactions);

        mLineChart.setDrawGridBackground(true);
        mLineChart.getDescription().setEnabled(false);

        LineDataSet dataSet = new LineDataSet(entries, "Chi tiêu");


        ArrayList<ILineDataSet> datasets = new ArrayList<>();
        datasets.add(dataSet);

        LineData lineData = new LineData(dataSet);
        mLineChart.setData(lineData);
        mLineChart.setMinimumHeight(500);
        mLineChart.invalidate();

        String values[] = new String[entries.size()];
        for (int i = 0; i < values.length; ++i) {
            values[i] = " ";
        }

        values[0] = mBudget.getTimeStart().toIsoDateString();
        values[values.length - 1] = mBudget.getTimeEnd().toIsoDateString();
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(values));

        mLineChart.setMaxHighlightDistance(mBudget.getAmount());

        mLineChart.getAxisLeft().setAxisMinimum(0.0f);
        mLineChart.getAxisLeft().setAxisMaximum(entries.get(entries.size() - 1).getY() + 200);

        mLineChart.getAxisRight().setEnabled(false);

        LimitLine limitLine = new LimitLine(mBudget.getAmount(), "Ngân sách");
        limitLine.setLineWidth(4f);
        limitLine.enableDashedLine(10f, 10f, 0f);
        limitLine.setTextSize(10f);
        mLineChart.getAxisLeft().addLimitLine(limitLine);


    }

    private List<Entry> filterAmountByDates(List<Transaction> transactions) {
        long start = mBudget.getTimeStart().getMillis();
        long end = mBudget.getTimeEnd().getMillis();

        int total_day = (int) Math.ceil((end - start) / 24 / 60 / 60 / 1000.0f);
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i <= total_day; ++i) {
            entries.add(new Entry(i, 0.0f));
        }

        for (Transaction t : transactions) {
            long current = t.getTransactionDate().getTime();
            int index = (int) Math.ceil((current - start) / 24 / 60 / 60 / 1000.0f);
            entries.get(index).setY(entries.get(index).getY() + t.getTransactionTrading());

        }

        float total = 0.0f;
        for (int i = 0; i < entries.size(); ++i) {
            total += entries.get(i).getY();
            entries.get(i).setY(total);
        }

        return entries;
    }
}
