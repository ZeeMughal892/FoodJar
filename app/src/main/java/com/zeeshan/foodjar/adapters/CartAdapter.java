package com.zeeshan.foodjar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeeshan.foodjar.R;
import com.zeeshan.foodjar.entities.Order;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private List<Order> orderList;

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemNameCart, itemCategoryCart, itemPriceCart, itemQuantityCart;
        String itemID;
        ImageView imgItemCart;


        MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            itemNameCart = itemView.findViewById(R.id.txtNameCart);
            itemCategoryCart = itemView.findViewById(R.id.txtCategoryCart);
            itemPriceCart = itemView.findViewById(R.id.txtPriceCart);
            itemQuantityCart = itemView.findViewById(R.id.txtQuantityCart);
            imgItemCart = itemView.findViewById(R.id.imgItemCart);

        }

        @Override
        public void onClick(View view) {

        }
    }

    public CartAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_cart, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Order order = orderList.get(position);
        String Stock = " " + order.getItemQuantity() + " " + order.getItemUnit();
        String Price = "Rs. " + order.getItemPrice();

        holder.itemNameCart.setText(order.getItemName());
        holder.itemCategoryCart.setText(order.getItemCategory());
        holder.itemPriceCart.setText(Price);
        holder.itemQuantityCart.setText(Stock);

        Picasso.get()
                .load(order.getItemImage())
                .placeholder(R.drawable.ic_noimage)
                .fit()
                .centerCrop()
                .into(holder.imgItemCart);

        holder.itemID = order.getItemID();
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
