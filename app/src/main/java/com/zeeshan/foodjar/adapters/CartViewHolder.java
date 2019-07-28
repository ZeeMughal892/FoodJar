package com.zeeshan.foodjar.adapters;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.service.Common;
import com.zeeshan.foodjar.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView itemNameCart, itemCategoryCart, itemPriceCart, itemQuantityCart;
    public ImageView imgItemCart;
    public RelativeLayout view_background;
    public LinearLayout view_foreground;
    String itemID;

    public CartViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        itemNameCart = itemView.findViewById(R.id.txtNameCart);
        itemCategoryCart = itemView.findViewById(R.id.txtCategoryCart);
        itemPriceCart = itemView.findViewById(R.id.txtPriceCart);
        itemQuantityCart = itemView.findViewById(R.id.txtQuantityCart);
        imgItemCart = itemView.findViewById(R.id.imgItemCart);
        view_background = itemView.findViewById(R.id.view_background);
        view_foreground = itemView.findViewById(R.id.view_foreground);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select action");
    }
}