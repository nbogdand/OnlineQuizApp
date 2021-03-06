package com.example.bogdan.onlinequiz.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.bogdan.onlinequiz.Interface.ItemClickListener;
import com.example.bogdan.onlinequiz.R;

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txt_name, txt_score;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public RankingViewHolder(View itemView) {
        super(itemView);
        txt_name = (TextView) itemView.findViewById(R.id.txt_name);
        txt_score = (TextView) itemView.findViewById(R.id.txt_score);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
