package com.minhthuanht.quanlytaichinh.overviewtransaction.fragment;

import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.implementDAO.ITransactionsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.IWalletsDAO;
import com.minhthuanht.quanlytaichinh.implementDAO.TransactionsDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.WalletsDAOimpl;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.model.Wallet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FragmentTendency extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private Spinner mSpinnerItems;

    private Spinner mSpinnerSelect;

    private int mSpItemsClick;

    private int mSpSelectClick;

    private List<Transaction> mListTransaction = new ArrayList<>();

    private final AdapterView.OnItemSelectedListener mSpinnerItemsListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            mSpItemsClick = position;
            handleItemClick();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    private final AdapterView.OnItemSelectedListener mSpinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            mSpSelectClick = position;
            handleItemClick();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


//    private Spinner mTimeStart;
//
//    private Spinner mTimeEnd;

    public FragmentTendency() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tendency, container, false);
        mSpinnerItems = view.findViewById(R.id.spinnerItems);
        mSpinnerSelect = view.findViewById(R.id.spinnerSelect);
//        mTimeStart = view.findViewById(R.id.spinnerTimeStart);
//        mTimeEnd = view.findViewById(R.id.spinnerTimeEnd);

        ArrayAdapter<CharSequence> adapterItems = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.spinnerItems, android.R.layout.simple_spinner_item);
        adapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerItems.setAdapter(adapterItems);

        ArrayAdapter<CharSequence> adapterSelect = ArrayAdapter.createFromResource(getContext(), R.array.spinnerSelect, android.R.layout.simple_spinner_item);
        adapterSelect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSelect.setAdapter(adapterSelect);

        initData();

        initEvents();

        return view;
    }

    private void initData() {

        IWalletsDAO iWalletsDAO = new WalletsDAOimpl(getContext());
        ITransactionsDAO iTransactionsDAO = new TransactionsDAOimpl(getContext());
        Wallet wallet = iWalletsDAO.getAllWalletByUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).get(0);
        mListTransaction = iTransactionsDAO.getAllTransactionByWalletId(wallet.getWalletID());

    }

    private void initEvents() {

        mSpinnerItems.setOnItemSelectedListener(mSpinnerItemsListener);
        mSpinnerSelect.setOnItemSelectedListener(mSpinnerSelectedListener);

    }


    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private void handleItemClick() {

        List<Transaction> tranEx = new ArrayList<>();
        List<Transaction> tranIn = new ArrayList<>();
        Fragment fragment;

        for (Transaction tran : mListTransaction) {

            if (tran.getMoneyTradingWithSign() < 0) {

                tranEx.add(tran);

            } else {

                tranIn.add(tran);
            }
        }

        switch (mSpSelectClick) {

            case 0:

                switch (mSpItemsClick) {

                    case 2:
                        fragment = FragmentPieChartOV.newInstance(mListTransaction, 2);
                        assert fragment != null;
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();

                        break;

                    case 0:
                        fragment = FragmentPieChartOV.newInstance(tranEx, 0);
                        assert fragment != null;
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();

                        break;

                    case 1:
                        fragment = FragmentPieChartOV.newInstance(tranIn, 1);
                        assert fragment != null;
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();

                        break;

                }

                break;

            case 1:

                switch (mSpItemsClick) {

                    case 2:
                        Toast.makeText(getContext(), getString(R.string.feature_developing), Toast.LENGTH_SHORT).show();
//                        fragment = FragmentBarChartOv.newInstance(mListTransaction, 2);
//                        assert fragment != null;
//                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();
//
                        break;

                    case 0:
                        fragment = FragmentBarChartOv.newInstance(tranEx, 0);
                        assert fragment != null;
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();

                        break;

                    case 1:
                        fragment = FragmentBarChartOv.newInstance(tranIn, 1);
                        assert fragment != null;
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();

                        break;

                }

                break;


        }

//        if (mSpSelectClick == 0) {
//
//            Fragment fragment;
//
//            switch (mSpItemsClick) {
//
//                case 2:
//                    fragment = FragmentPieChartOV.newInstance(mListTransaction, 2);
//                    assert fragment != null;
//                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();
//
//                    break;
//
//                case 0:
//                    fragment = FragmentPieChartOV.newInstance(mListTransaction, 0);
//                    assert fragment != null;
//                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();
//
//                    break;
//
//                case 1:
//                    fragment = FragmentPieChartOV.newInstance(mListTransaction, 1);
//                    assert fragment != null;
//                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();
//
//                    break;
//
//            }
//        }
    }

}
