package com.palgao.menu.modules.products.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.palgao.menu.R;

import java.util.List;

// CategoryAdapter.java
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categorys;
    private OnCategoryClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private Context context;
    public interface OnCategoryClickListener {
        void onCategoryClick(String categorys);
    }

    public CategoryAdapter(List<String> categorys, OnCategoryClickListener listener, Context context) {
        this.context = context;
        this.categorys = categorys;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categorys.get(position);
        holder.bind(category);

        if (holder.getAdapterPosition() == selectedPosition) {
            holder.tv_categoryName.setBackgroundResource(R.drawable.wa_buttom_filter);
            holder.tv_categoryName.setTextColor(context.getColor(R.color.wa_primary));
        } else {
            holder.tv_categoryName.setBackgroundResource(R.drawable.business_category_background);
            holder.tv_categoryName.setTextColor(context.getColor(R.color.action_button_selected));
        }

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
            listener.onCategoryClick(category);
        });
    }

    @Override
    public int getItemCount() {
        if (categorys != null)
            return categorys.size();
        else
            return 0;
    }

    public void updateCategory(List<String> newCategoryes) {
        this.categorys = newCategoryes;
        notifyDataSetChanged();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView tv_categoryName;
        ImageView iv_closet;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_categoryName = itemView.findViewById(R.id.tv_categoryName);
            iv_closet = itemView.findViewById(R.id.iv_closet);
        }

        public void bind(String categorys) {
            tv_categoryName.setText(categorys);
        }
    }
}


