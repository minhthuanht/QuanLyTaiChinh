package com.minhthuanht.quanlytaichinh.chart.adapter;

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
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.implementDAO.CategoriesDAOimpl;
import com.minhthuanht.quanlytaichinh.implementDAO.ICategoriesDAO;
import com.minhthuanht.quanlytaichinh.model.Category;
import com.minhthuanht.quanlytaichinh.view.CurrencyTextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChartOVTransactionAdapter extends RecyclerView.Adapter<ChartOVTransactionAdapter.ViewHoder> {

    private List<Pair<Float, String>> mItems = new ArrayList<>();

    private ICategoriesDAO mICategoriesDAO;

    private AssetManager mAssetManager;

    public ChartOVTransactionAdapter(List<Pair<Float, String>> totalItems) {

        if (totalItems != null) {

            mItems.addAll(totalItems);
        }
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        mAssetManager = context.getAssets();
        mICategoriesDAO = new CategoriesDAOimpl(context);

        View view = LayoutInflater.from(context).inflate(R.layout.chart_ov_transaction_adpter, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {

        Pair<Float, String> item = mItems.get(position);
        holder.onBindView(item);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHoder extends RecyclerView.ViewHolder {

        private ImageView mImgItem;

        private TextView mLabelItem;

        private CurrencyTextView mTradingItem;

        ViewHoder(@NonNull View itemView) {
            super(itemView);

            mImgItem = itemView.findViewById(R.id.imgItem);
            mLabelItem = itemView.findViewById(R.id.txtLable);
            mTradingItem = itemView.findViewById(R.id.txtTranding);
        }

        void onBindView(Pair<Float, String> item) {

            mLabelItem.setText(item.second);
            mTradingItem.setText(String.valueOf(item.first));

            Category category = mICategoriesDAO.getCategoryByName(item.second);

            // lấy icon
            String path_icon = "category/";
            InputStream istr = null;
            try {
                istr = mAssetManager.open(path_icon + category.getCategoryIcon());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(istr);
            mImgItem.setImageBitmap(bitmap);
        }
    }
}
