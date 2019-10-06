package com.minhthuanht.quanlytaichinh.budget.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.budget.activity.AddBudgetActivity;
import com.minhthuanht.quanlytaichinh.implementDAO.BudgetDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.IBudgetDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.ITransactionsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.IWalletsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.TransactionsDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.WalletsDAOimpl;
import com.minhthuanht.quanlytaichinh.model.Budget;
import com.minhthuanht.quanlytaichinh.model.DateRange;
import com.minhthuanht.quanlytaichinh.model.MTDate;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.model.Wallet;
import com.minhthuanht.quanlytaichinh.budget.adapter.BudgetPagerAdapter;
import com.minhthuanht.quanlytaichinh.utilities.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FragmentBudget extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "FragmentBudget";

    private static final int ADD_BUDGET = 101;

    private FloatingActionButton mButtonAdd;

    private List<Budget> mListBudget = new ArrayList<>();

    private BudgetPagerAdapter mAdapter;

    private final View.OnClickListener mButtonAddListener = view -> {

        addBudgetActivity();

    };


    public FragmentBudget() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_budget, container, false);
        // TODO: Rename and change types of parameters
        PagerSlidingTabStrip mTabLayout = view.findViewById(R.id.tab_layout);
        ViewPager mViewPager = view.findViewById(R.id.viewPager);
        mButtonAdd = view.findViewById(R.id.addBudget);

        mAdapter = new BudgetPagerAdapter(getChildFragmentManager(), mListBudget);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);

        initEvents();


        return view;
    }

    @Override
    public void onResume() {

        Objects.requireNonNull(getActivity()).setTitle(R.string.budget);
        super.onResume();
        updateUI();

    }

    private void updateUI() {

        IWalletsDAO iWalletsDAO = new WalletsDAOimpl(getContext());
        Wallet wallet = iWalletsDAO.getAllWalletByUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).get(0);

        IBudgetDAO iBudgetDAO = new BudgetDAOimpl(getContext());

        updateBudget(wallet.getWalletID(), iBudgetDAO);
        List<Budget> budgetList = iBudgetDAO.getAllBudget(wallet.getWalletID());

        mListBudget.clear();
        if (budgetList != null) {
            mListBudget.addAll(budgetList);
        }
        mAdapter.updateItems(mListBudget);

    }

    private void initEvents() {
        mButtonAdd.setOnClickListener(mButtonAddListener);
    }

    private void addBudgetActivity() {

        Intent intent = new Intent(getContext(), AddBudgetActivity.class);
        startActivityForResult(intent, ADD_BUDGET);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_BUDGET && resultCode == Activity.RESULT_OK) {

            Bundle bundle = data.getBundleExtra(AddBudgetActivity.RESULT_ADD);
            assert bundle != null;
            Budget budget = bundle.getParcelable(AddBudgetActivity.RESULT_ADD);
            mListBudget.add(budget);
        }
    }

    private void updateBudget(int walletID, IBudgetDAO iBudgetDAO) {

        ITransactionsDAO transactionsDAO = new TransactionsDAOimpl(getContext());
        List<Transaction> transactions = transactionsDAO.getAllTransactionByWalletId(walletID);
        DateUtils dateUtils = new DateUtils();
        List<Budget> budgetList = iBudgetDAO.getAllBudget(walletID);

        for (Budget budget : budgetList){

            DateRange dateRange = new DateRange(budget.getTimeStart(), budget.getTimeEnd());

            float spent = 0;

            for (Transaction transaction : transactions){

                if (budget.getCategory().getCategoryID() == transaction.getTransactionCategoryID().getCategoryID()
                && dateUtils.isDateRangeContainDate(dateRange, transaction.getTransactionDate())){

                    spent += Math.abs(transaction.getMoneyTradingWithSign());

                    budget.setSpent(spent);
                    iBudgetDAO.updateBudget(budget);
                }
            }
        }

    }


}
