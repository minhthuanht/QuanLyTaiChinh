package com.minhthuanht.quanlytaichinh.budget.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.budget.adapter.SelectTimeAdapter;
import com.minhthuanht.quanlytaichinh.model.DateRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectTimeActivity extends AppCompatActivity {

    public static final String CODE_RESPONSE = "response_AddBudgetActivity";


    private SelectTimeAdapter.IItemClickedInterface mItemClickedInterface = dateRange -> {

        Intent intent = getIntent();
        intent.putExtra(CODE_RESPONSE, dateRange);
        setResult(RESULT_OK, intent);
        finish();
    };

    private List<String> mTimeBudget = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        intControls();
    }

    private void intControls() {

        RecyclerView mRecyclerView = findViewById(R.id.recycleTime);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        llm.canScrollVertically();
        mRecyclerView.setLayoutManager(llm);

        mTimeBudget.addAll(getBudgetTimeRange());
        SelectTimeAdapter adapter = new SelectTimeAdapter(mTimeBudget, mItemClickedInterface);
        mRecyclerView.setAdapter(adapter);

    }

    private ArrayList<String> getBudgetTimeRange() {
        ArrayList<String> timeRanges = new ArrayList<>();
        timeRanges.add(getString(R.string.thisweek));
        timeRanges.add(getString(R.string.thismonth));
        timeRanges.add(getString(R.string.thisquarter));
        timeRanges.add(getString(R.string.thisyear));
        timeRanges.add(getString(R.string.nextweek));
        timeRanges.add(getString(R.string.nextmonth));
        timeRanges.add(getString(R.string.nextquarter));
        timeRanges.add(getString(R.string.nextyear));
        return timeRanges;
    }

    @Override
    public boolean onSupportNavigateUp() {

        setResult(RESULT_CANCELED);
        finish();
        return true;
    }
}
