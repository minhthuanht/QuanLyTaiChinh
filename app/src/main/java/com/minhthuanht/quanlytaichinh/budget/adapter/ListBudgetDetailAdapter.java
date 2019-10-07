package com.minhthuanht.quanlytaichinh.budget.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.model.Budget;
import com.minhthuanht.quanlytaichinh.model.DateRange;
import com.minhthuanht.quanlytaichinh.model.MTDate;
import com.minhthuanht.quanlytaichinh.view.CurrencyTextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ListBudgetDetailAdapter extends RecyclerView.Adapter<ListBudgetDetailAdapter.ViewHolder> {

    public interface IItemClikedDAO{

        void itemClicked(Budget budget);
    }

    private IItemClikedDAO mIItemClikedDAO;

    private List<Budget> mItems = new ArrayList<>();

    private AssetManager mAssetManager;

    public ListBudgetDetailAdapter(List<Budget> budgets, IItemClikedDAO itemClikedDAO) {

        if (budgets != null && itemClikedDAO != null) {

            mItems.addAll(budgets);
            mIItemClikedDAO = itemClikedDAO;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        mAssetManager = context.getAssets();
        View view = LayoutInflater.from(context).inflate(R.layout.item_budget, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Budget budget = mItems.get(position);
        holder.onBindView(budget);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImgGoal;

        private ImageView mImgWallet;

        private TextView mBudgetTitle;

        private TextView mTimeBudget;

        private TextView mCurrent;

        private CurrencyTextView mAmountLeft;

        private TextView mTimeLeft;

        private ProgressBar mPrgBudget;

        private TextView mTextBudget1;

        private TextView mTextBudget2;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImgGoal = itemView.findViewById(R.id.icon_goal);
            mImgWallet = itemView.findViewById(R.id.iconWallet);
            mBudgetTitle = itemView.findViewById(R.id.txtBudgetTitle);
            mTimeBudget = itemView.findViewById(R.id.txtTimeRanger);
            mTimeLeft = itemView.findViewById(R.id.txtTimeLeft);
            mCurrent = itemView.findViewById(R.id.current);
            mAmountLeft = itemView.findViewById(R.id.txtAmountLeft);
            mPrgBudget = itemView.findViewById(R.id.prgBudget);
            mTextBudget1 = itemView.findViewById(R.id.textBudget1);
            mTextBudget2 = itemView.findViewById(R.id.textBudget2);

            itemView.setOnClickListener(view -> mIItemClikedDAO.itemClicked(mItems.get(getAdapterPosition())));


        }

        @SuppressLint("ResourceAsColor")
        public void onBindView(Budget budget) {

            String path_icon = "category/";
            InputStream inputStreamGoal = null;
            InputStream inputStreamWallet = null;
            try {
                inputStreamGoal = mAssetManager.open(path_icon + budget.getCategory().getCategoryIcon());
                inputStreamWallet = mAssetManager.open(path_icon + "icon.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputStreamGoal != null) {

                Bitmap bitmapGoal = BitmapFactory.decodeStream(inputStreamGoal);
                mImgGoal.setImageBitmap(bitmapGoal);
            }
            if (inputStreamWallet != null) {

                Bitmap bitmapWallet = BitmapFactory.decodeStream(inputStreamWallet);
                mImgWallet.setImageBitmap(bitmapWallet);
            }

            mBudgetTitle.setText(budget.getCategory().getCategory());
            mTimeBudget.setText(new DateRange(budget.getTimeStart(), budget.getTimeEnd()).toString());

            MTDate now = new MTDate();
            long remainDays = (long) Math.ceil((budget.getTimeEnd().getMillis() - now.getMillis()) / 24 / 60 / 60 / 1000.0f);
            if (remainDays > 0) {

                mTimeLeft.setText(String.valueOf(remainDays));
            }

            else {

                mTextBudget1.setVisibility(View.GONE);
                mTextBudget2.setVisibility(View.GONE);
                mTimeLeft.setText(R.string.time_out);
            }



            float remain = budget.getAmount() - budget.getSpent();
            if (remain < 0) {
                remain = Math.abs(remain);
                mCurrent.setText(R.string.overspending);
            } else {
                mCurrent.setText(R.string.remain);
            }
            mAmountLeft.setText(String.valueOf(remain));

            mPrgBudget.setMax(100);
            int process = (int) (budget.getSpent() / budget.getAmount() * 100);
            mPrgBudget.setProgress(process);

        }
    }

}
