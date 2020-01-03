package com.minhthuanht.quanlytaichinh.budget.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.budget.activity.DetailBudgetActivity;
import com.minhthuanht.quanlytaichinh.budget.adapter.ListBudgetDetailAdapter;
import com.minhthuanht.quanlytaichinh.implementDAO.BudgetDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.IBudgetDAO;
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

    private IBudgetDAO mIBudgetDAO;

    private RecyclerView mRecyclerView;

    private LinearLayout mLinearLayout;

    private ListBudgetDetailAdapter.IItemClikedDAO mItemClikedDAO = new ListBudgetDetailAdapter.IItemClikedDAO() {
        @Override
        public void itemClicked(Budget budget) {
            Intent intent = new Intent(getContext(), DetailBudgetActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(REQUEST_DETAIL_BUDGET, budget);
            intent.putExtra(REQUEST_DETAIL_BUDGET, bundle);
            startActivityForResult(intent, REQUEST_CODE);
        }

        @Override
        public boolean itemLongClicked(Budget budget) {
            return deleteBudget(budget);
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
        if (budgetList != null) {

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
    public void onResume() {
        super.onResume();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_budget, container, false);
        // TODO: Rename and change types of parameters
        mLinearLayout = view.findViewById(R.id.layout_transaction_empty);
        if (mBudgets.size() > 0) {
            mLinearLayout.setVisibility(View.INVISIBLE);
        }
        mRecyclerView = view.findViewById(R.id.recycleViewOV);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        llm.canScrollVertically();
        mRecyclerView.setLayoutManager(llm);
        ListBudgetDetailAdapter adapter = new ListBudgetDetailAdapter(mBudgets, mItemClikedDAO);
        mRecyclerView.setAdapter(adapter);


        return view;
    }

    private boolean deleteBudget(Budget budget) {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle(getResources().getString(R.string.title_delete_budget));
        builder.setMessage(getResources().getString(R.string.messing_delete));
        builder.setPositiveButton(getResources().getString(R.string.comfirm), (dialogInterface, i) -> {

            mIBudgetDAO = new BudgetDAOimpl(getContext());
            mIBudgetDAO.deleteBudget(budget);
            List<Budget> budgets = mIBudgetDAO.getAllBudget();
            ListBudgetDetailAdapter adapter = new ListBudgetDetailAdapter(budgets, mItemClikedDAO);
            mRecyclerView.setAdapter(adapter);
            if (budgets.size() <= 0) {
                mLinearLayout.setVisibility(View.VISIBLE);
            }
            dialogInterface.dismiss();
        });

        builder.setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
        return true;
    }

}
