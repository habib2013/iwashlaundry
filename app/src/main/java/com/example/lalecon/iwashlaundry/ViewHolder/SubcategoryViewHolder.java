package com.example.lalecon.iwashlaundry.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lalecon.iwashlaundry.Interface.ItemClickListener;
import com.example.lalecon.iwashlaundry.R;

/**
 * Created by Lalecon on 5/31/2018.
 */

public class SubcategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView subcategory_name;
    public ImageView subcategory_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SubcategoryViewHolder(View itemView) {
        super(itemView);

        subcategory_name = (TextView) itemView.findViewById(R.id.subcategory_name);
        subcategory_image = (ImageView) itemView.findViewById(R.id.subcategory_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
