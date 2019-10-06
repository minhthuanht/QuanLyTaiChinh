package com.minhthuanht.quanlytaichinh.budget.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.budget.activity.DetailBudgetActivity;
import com.minhthuanht.quanlytaichinh.budget.adapter.ListBudgetDetailAdapter;
import com.minhthuanht.quanlytaichinh.model.Budget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FragmentListBudget extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "FragmentListBudget";

    private static final String ARG_ITEM = "items";

    private static final String REQUEST_DETAIL_BUDGET = "sendDetailBudgetActivity";

    private final int REQUEST_CODE = 111;

    private ListBudgetDetailAdapter.IItemClikedDAO mItemClikedDAO = new ListBudgetDetailAdapter.IItemClikedDAO() {
        @Override
        public void itemClicked(Budget budget) {

            Intent intent = new Intent(getContext(), DetailBudgetActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(REQUEST_DETAIL_BUDGET,budget);
            intent.putExtra(REQUEST_DETAIL_BUDGET, bundle);
            startActivityForResult(intent, REQUEST_CODE);
        }
    };

    private List<Budget> mBudgets = new ArrayList<>();




    public FragmentListBudget() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentListBudget newInstance(List<Budget> budgetList) {
        FragmentListBudget fragment = new FragmentListBudget();
        Bundle args = new Bundle();
        if (budgetList != null){

            args.putParcelableArrayList(ARG_ITEM, (ArrayList<? extends Parcelable>) budgetList);
        }
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mBudgets.addAll(Objects.requireNonNull(getArguments().getParcelableArrayList(ARG_ITEM)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_fragment_list_budget, container, false);
        // TODO: Rename and change types of parameters
        LinearLayout linearLayout = view.findViewById(R.id.layout_transaction_empty);
        RecyclerView recyclerView = view.findViewById(R.id.recycleViewOV);

        if (mBudgets.size() > 0){
            linearLayout.setVisibility(View.INVISIBLE);
        }
        recyclerView.hasFixedSize();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        llm.canScrollVertically();
        recyclerView.setLayoutManager(llm);
        ListBudgetDetailAdapter adapter = new ListBudgetDetailAdapter(mBudgets, mItemClikedDAO);
        recyclerView.setAdapter(adapter);



        return view;
    }

}
