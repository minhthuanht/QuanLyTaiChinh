package com.minhthuanht.quanlytaichinh.overviewtransaction.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.model.Transaction;
import com.minhthuanht.quanlytaichinh.view.CurrencyTextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListTransactionPBAdapter extends RecyclerView.Adapter<ListTransactionPBAdapter.ViewHoder> {

    private List<Transaction> mItems = new ArrayList<>();

    private AssetManager mAssetManager;

    private SimpleDateFormat mFormat;

    public ListTransactionPBAdapter(List<Transaction> items) {

        if (items != null) {

            mItems.addAll(items);
        }
    }

    @SuppressLint("SimpleDateFormat")
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        mAssetManager = context.getAssets();
        mFormat = new SimpleDateFormat(context.getResources().getString(R.string.format_date));
        View view = LayoutInflater.from(context).inflate(R.layout.snippet_transaction_item6, parent, false);

        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {

        Transaction transaction = mItems.get(position);
        holder.onBindView(transaction);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHoder extends RecyclerView.ViewHolder {

        private ImageView mImgCategory;

        private TextView mLable;

        private TextView mDate;

        private CurrencyTextView mTrading;

        ViewHoder(@NonNull View itemView) {
            super(itemView);

            mImgCategory = itemView.findViewById(R.id.imgTransactionItem);
            mLable = itemView.findViewById(R.id.txtTransactionLable);
            mDate = itemView.findViewById(R.id.txtTransactionDate);
            mTrading = itemView.findViewById(R.id.txtTrandingItem);
        }

        public void onBindView(Transaction transaction) {

            mLable.setText(transaction.getTransactionCategoryID().getCategory());
            mTrading.setText(String.valueOf(transaction.getTransactionTrading()));

            String path_icon = "category/";
            InputStream inputStream = null;
            try {
                inputStream = mAssetManager.open(path_icon + transaction.getTransactionCategoryID().getCategoryIcon());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            mImgCategory.setImageBitmap(bitmap);
            mDate.setText(mFormat.format(transaction.getTransactionDate()));

        }
    }
}
