package com.minhthuanht.quanlytaichinh.overviewtransaction.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.google.firebase.auth.FirebaseAuth;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.implementDAO.ITransactionsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.IWalletsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.TransactionsDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.WalletsDAOimpl;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.model.Wallet;
import com.minhthuanht.quanlytaichinh.overviewtransaction.adapter.PayBookPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentPayBook extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "FragmentPayBook";

    private ITransactionsDAO mITransactionsDAO ;


    public FragmentPayBook() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_book, container, false);
        PagerSlidingTabStrip tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);

        IWalletsDAO iWalletsDAO = new WalletsDAOimpl(getContext());
        ITransactionsDAO iTransactionsDAO = new TransactionsDAOimpl(getContext());
        Wallet wallet = iWalletsDAO.getAllWalletByUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).get(0);
        List<Transaction> mItems = iTransactionsDAO.getAllTransactionByWalletId(wallet.getWalletID());

        PayBookPagerAdapter adapter = new PayBookPagerAdapter(getChildFragmentManager(), mItems);
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.pay_book);
    }
}
