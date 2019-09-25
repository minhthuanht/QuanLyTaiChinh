package com.minhthuanht.quanlytaichinh.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.pinnedheaderlistview.SectionedBaseAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TransactionListAdapter extends SectionedBaseAdapter {

    private List<Pair<Date, List<Transaction>>> mItems = new ArrayList<>();

    public TransactionListAdapter(List<Pair<Date, List<Transaction>>> filterItem) {

        if (filterItem != null) {

            mItems.addAll(filterItem);
        }
    }

    public void updateValues(List<Pair<Date, List<Transaction>>> items) {

        mItems.clear();
        if (items != null) {

            mItems.addAll(items);
        }
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int section, int position) {
        return mItems.get(section).second.get(position);
    }

    @Override
    public long getItemId(int section, int position) {

        int res = 0;
        for (int i = 0; i < section - 1; i++) {
            res += mItems.get(i).second.size();
            res += 1;
        }
        return res + position;
    }

    @Override
    public int getSectionCount() {
        return mItems.size();
    }

    @Override
    public int getCountForSection(int section) {
        return mItems.get(section).second.size();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {

        RelativeLayout layout;
        Context context = parent.getContext();
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (RelativeLayout) inflater.inflate(R.layout.snippet_transaction_item, null);
        } else {

            layout = (RelativeLayout) convertView;
        }

        Transaction transaction = mItems.get(section).second.get(position);
        ItemViewHolder holder = new ItemViewHolder(layout);
        holder.onBindView(transaction,context );

        return layout;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

        RelativeLayout layout;
        Context context = parent.getContext();
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (RelativeLayout) inflater.inflate(R.layout.snippet_transaction_header, null);
        } else {

            layout = (RelativeLayout) convertView;
        }

        HeaderViewHolder holder = new HeaderViewHolder(layout);
        holder.onBindView(mItems, section, context);

        return layout;
    }

    class HeaderViewHolder {
        private TextView mHeaderDate;
        private TextView mHeaderDay;
        private TextView mHeaderMonthYear;
        private TextView mHeaderMoneyTrading;

        HeaderViewHolder(View itemView) {

            mHeaderDate = itemView.findViewById(R.id.txtDateHeader);
            mHeaderDay = itemView.findViewById(R.id.txtDayHeader);
            mHeaderMonthYear = itemView.findViewById(R.id.txtMonthYear);
            mHeaderMoneyTrading = itemView.findViewById(R.id.txtTrandingHeader);
        }


        void onBindView(List<Pair<Date, List<Transaction>>> items, int section, Context context) {

            Date date = items.get(section).first;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
            mHeaderDate.setText(simpleDateFormat.format(date));
            simpleDateFormat = new SimpleDateFormat("EEEE");
            mHeaderDay.setText(simpleDateFormat.format(date));
            simpleDateFormat = new SimpleDateFormat("MM/yyyy");
            mHeaderMonthYear.setText(simpleDateFormat.format(date));

            float moneyTrading = 0;

            List<Transaction> transactions = items.get(section).second;
            for (Transaction tran : transactions) {

                moneyTrading += tran.getMoneyTradingWithSign();
            }

            String moneyHeader = String.valueOf(moneyTrading);
            mHeaderMoneyTrading.setText(moneyHeader);
        }
    }

    class ItemViewHolder {
        private ImageView mItemImage;
        private TextView mItemLabel;
        private TextView mItemNote;
        private TextView mItemMoneyTrading;

        ItemViewHolder(View itemView) {

            mItemImage = itemView.findViewById(R.id.imgTransactionItem);
            mItemLabel = itemView.findViewById(R.id.txtTransactionLable);
            mItemNote = itemView.findViewById(R.id.txtTransactionNote);
            mItemMoneyTrading = itemView.findViewById(R.id.txtTrandingItem);
        }

        void onBindView(Transaction transaction, Context context) {

            mItemLabel.setText(transaction.getTransactionCategoryID().getCategory());
            mItemNote.setText(transaction.getTransactionNote());
            mItemMoneyTrading.setText(String.valueOf(transaction.getTransactionTrading()));

            // lay anh tu asset
            String base_path = "category/";
            try {

                Drawable img = Drawable.createFromStream(context.getAssets().open(base_path + transaction.getTransactionCategoryID().getCategoryIcon()), null);
                mItemImage.setImageDrawable(img);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (transaction.getMoneyTradingWithSign() >= 0) {
                mItemMoneyTrading.setTextColor(context.getResources().getColor(R.color.colorMoneyTradingPositive));
            } else {
                mItemMoneyTrading.setTextColor(context.getResources().getColor(R.color.colorMoneyTradingNegative));
            }
        }
    }

}
