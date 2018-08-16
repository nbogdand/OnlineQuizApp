package com.example.bogdan.onlinequiz.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bogdan.onlinequiz.Interface.ItemClickListener;
import com.example.bogdan.onlinequiz.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView category_name;
    public ImageView category_image;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        category_name = (TextView) itemView.findViewById(R.id.category_name);
        category_image = (ImageView) itemView.findViewById(R.id.category_image);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}


