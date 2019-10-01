package com.minhthuanht.quanlytaichinh.overviewtransaction.fragment;


import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.implementDAO.CategoriesDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.ICategoriesDAO;
import com.minhthuanht.quanlytaichinh.model.Category;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.overviewtransaction.adapter.ListTransactionPBAdapter;
import com.minhthuanht.quanlytaichinh.transaction.fragment.FragmentListTransaction;
import com.minhthuanht.quanlytaichinh.view.CurrencyTextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FragmentPagerPB extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "FragmentPagerPB";

    private static final String ARG_ITEMS = "items";

    private static final String ARG_NUMBER = "numberID";

    // TODO: Rename and change types of parameters

    private List<Transaction> mItems = new ArrayList<>();

    private int mNumberID;

    private TextView mTextNotePBDebt;

    private TextView mTextNotePBLoan;

    private CurrencyTextView mTradingMoney;

    private CardView mCardView;

    private ImageView mImgCategory;

    private TextView mNumberTransaction;

    private CurrencyTextView mMoneyTotal;

    private RecyclerView mRecyclerViewOV;

    private final View.OnClickListener mCardViewListener = view -> itemOnClick();

    public FragmentPagerPB() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentPagerPB newInstance(List<Transaction> items, int numberID) {
        FragmentPagerPB fragment = new FragmentPagerPB();
        Bundle args = new Bundle();
        if (items != null) {

            args.putParcelableArrayList(ARG_ITEMS, (ArrayList<? extends Parcelable>) items);
            args.putInt(ARG_NUMBER, numberID);

        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mItems.addAll(Objects.requireNonNull(getArguments().getParcelableArrayList(ARG_ITEMS)));
            mNumberID = getArguments().getInt(ARG_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager_paybook, container, false);
        mTextNotePBDebt = view.findViewById(R.id.txtTextNoteDebt);
        mTextNotePBLoan = view.findViewById(R.id.txtTextNoteLoan);
        mTradingMoney = view.findViewById(R.id.txtTradingPB);
        mCardView = view.findViewById(R.id.card_view1);
        mImgCategory = view.findViewById(R.id.imgCategory);
        mNumberTransaction = view.findViewById(R.id.txtNumberTransaction);
        mMoneyTotal = view.findViewById(R.id.txtMoneyTotal);
        mRecyclerViewOV = view.findViewById(R.id.recycleViewOV);

        initEvents();


        return view;
    }

    private void initEvents() {

        mCardView.setOnClickListener(mCardViewListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void itemOnClick() {


    }

    @Override
    public void onResume() {
        super.onResume();
        onBindView();
    }

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    private void onBindView() {

        if (mNumberID == 59) {

            mTextNotePBLoan.setVisibility(View.GONE);

            float moneyDebt = 0;
            int count = 0;
            for (Transaction tran : mItems) {

                moneyDebt += Math.abs(tran.getMoneyTradingWithSign());
                count++;
            }
            mTradingMoney.setText(String.valueOf(moneyDebt));
            mNumberTransaction.setText(String.valueOf(count));
            mMoneyTotal.setText(String.valueOf(moneyDebt));

            ICategoriesDAO iCategoriesDAO = new CategoriesDAOimpl(getContext());
            Category category = iCategoriesDAO.getCategoryById(59);
            String icon = category.getCategoryIcon();

            // lấy ảnh
            String path_icon = "category/";

            InputStream ins = null;
            AssetManager assetManager = Objects.requireNonNull(getContext()).getAssets();
            try {
                ins = assetManager.open(path_icon + icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ins != null) {

                Bitmap bitmap = BitmapFactory.decodeStream(ins);
                mImgCategory.setImageBitmap(bitmap);
            }


        } else if (mNumberID == 57) {

            mTextNotePBDebt.setVisibility(View.GONE);
            float moneyLoan = 0;
            int count = 0;
            for (Transaction tran : mItems) {

                moneyLoan += Math.abs(tran.getMoneyTradingWithSign());
                count++;
            }
            mTradingMoney.setText(String.valueOf(moneyLoan));
            mNumberTransaction.setText(String.valueOf(count));
            mMoneyTotal.setText(String.valueOf(moneyLoan));

            ICategoriesDAO iCategoriesDAO = new CategoriesDAOimpl(getContext());
            Category category = iCategoriesDAO.getCategoryById(57);
            String icon = category.getCategoryIcon();

            // lấy ảnh
            String path_icon = "category/";

            InputStream ins = null;
            AssetManager assetManager = Objects.requireNonNull(getContext()).getAssets();
            try {
                ins = assetManager.open(path_icon + icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ins != null) {

                Bitmap bitmap = BitmapFactory.decodeStream(ins);
                mImgCategory.setImageBitmap(bitmap);
            }

        }

        mRecyclerViewOV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.canScrollVertically();
        ListTransactionPBAdapter adapter = new ListTransactionPBAdapter(mItems);
        mRecyclerViewOV.setLayoutManager(llm);
        mRecyclerViewOV.setAdapter(adapter);
    }
}
