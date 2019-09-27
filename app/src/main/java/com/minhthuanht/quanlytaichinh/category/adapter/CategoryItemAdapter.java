package com.minhthuanht.quanlytaichinh.category.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ViewHolder> {

    public interface ICategoryAdapterListener{

        void onItemClicked(Category category);
    }

    private ICategoryAdapterListener mICategoryListener;

    private List<Category> mCategories = new ArrayList<>();

    public CategoryItemAdapter(List<Category> listCategories, ICategoryAdapterListener categoryInterface) {

        if (listCategories != null) {

            mCategories.addAll(listCategories);
        }
        if (categoryInterface != null){

            mICategoryListener = categoryInterface;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Category category = mCategories.get(position);
        holder.onBindView(category, holder);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImgIcon;

        private TextView mNameCategory;

        private View.OnClickListener mItemClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mICategoryListener != null){

                    mICategoryListener.onItemClicked(mCategories.get(getAdapterPosition()));
                }
            }
        };


        void onBindView(Category category, ViewHolder holder) {

            mNameCategory.setText(category.getCategory());

            // lấy ảnh từ asset

            String path_icon = "category/";
            try {

                Drawable drawable = Drawable.createFromStream(holder.mImgIcon.getContext().getAssets()
                        .open(path_icon + category.getCategoryIcon()), null);

                mImgIcon.setImageDrawable(drawable);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImgIcon = itemView.findViewById(R.id.imgCategory);
            mNameCategory = itemView.findViewById(R.id.txtCategory);

            itemView.setOnClickListener(mItemClicked);

        }
    }
}
