package com.zeeshan.foodjar.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zeeshan.foodjar.R;
import com.zeeshan.foodjar.SearchItem;
import com.zeeshan.foodjar.ShoppingCart;
import com.zeeshan.foodjar.database.Database;
import com.zeeshan.foodjar.entities.Order;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private List<Order> orderList;
    CardView cardViewCart;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemNameCart, itemCategoryCart, itemPriceCart, itemQuantityCart;
        String itemID;
        ImageView imgItemCart;


        MyViewHolder(View itemView) {
            super(itemView);

            itemNameCart = itemView.findViewById(R.id.txtNameCart);
            itemCategoryCart = itemView.findViewById(R.id.txtCategoryCart);
            itemPriceCart = itemView.findViewById(R.id.txtPriceCart);
            itemQuantityCart = itemView.findViewById(R.id.txtQuantityCart);
            imgItemCart = itemView.findViewById(R.id.imgItemCart);
            cardViewCart = itemView.findViewById(R.id.cardViewCart);
        }

    }

    public CartAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_cart, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(itemView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        cardViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("You want delete Item From Cart")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new Database(itemView.getContext()).removeFromCart(orderList.get(myViewHolder.getAdapterPosition()).getItemID(), firebaseUser.getUid());
                                itemView.getContext().startActivity(new Intent(itemView.getContext(), ShoppingCart.class));

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Order order = orderList.get(position);
        String Stock = " " + order.getItemQuantity() + " " + order.getItemUnit();
        String Price = "SAR " + order.getItemPrice();

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


    public void removeItem(int position) {
        orderList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item, int position) {
        orderList.add(position, item);
        notifyItemInserted(position);
    }
}
