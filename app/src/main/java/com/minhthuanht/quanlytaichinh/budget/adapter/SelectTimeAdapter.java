package com.minhthuanht.quanlytaichinh.budget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.model.DateRange;
import com.minhthuanht.quanlytaichinh.utilities.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectTimeAdapter extends RecyclerView.Adapter<SelectTimeAdapter.ViewHolder> {

    public interface IItemClickedInterface{

        void itemClicked(DateRange dateRange);
    }

    private List<String> mListTime = new ArrayList<>();

    private IItemClickedInterface mItemClickedInterface;

    public SelectTimeAdapter(List<String> timeRange, IItemClickedInterface itemClickedInterface) {

        if (timeRange != null && itemClickedInterface != null){

            mListTime.addAll(timeRange);
            mItemClickedInterface = itemClickedInterface;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String title = mListTime.get(position);
        holder.onBindView(title, holder);

    }

    @Override
    public int getItemCount() {
        return mListTime.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTime;

        private TextView mTimeRanger;

        private ImageView mImgChecked;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTime = itemView.findViewById(R.id.txtTitleTime);
            mTimeRanger = itemView.findViewById(R.id.txtDateRange);
            mImgChecked = itemView.findViewById(R.id.imgChecked);

            itemView.setOnClickListener(view -> {

                String title = mListTime.get(getLayoutPosition());
                DateUtils dateUtils = new DateUtils();
                DateRange dateRange = dateUtils.getDateRangeForPeriod(itemView.getContext(),title);

                mItemClickedInterface.itemClicked(dateRange);



            });
        }

        public void onBindView(String title, ViewHolder holder) {

            mTitleTime.setText(title);
            mTimeRanger.setText(getTimeBudget(title, holder));


        }
    }


    private String getTimeBudget(String title, ViewHolder holder) {

        DateUtils dateUtils = new DateUtils();
        DateRange dateRange = dateUtils.getDateRangeForPeriod(holder.itemView.getContext(),title);
        return dateRange.toString();
    }
}
