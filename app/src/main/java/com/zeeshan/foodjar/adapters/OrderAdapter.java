package com.zeeshan.foodjar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeeshan.foodjar.R;
import com.zeeshan.foodjar.entities.Order;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private List<Order> orderList;

    class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView txtItemNo,txtItemName,txtItemQty,txtItemUnit,txtItemPrice,txtItemAmount;


        MyViewHolder(View itemView) {
            super(itemView);
            txtItemNo=itemView.findViewById(R.id.txtItemNo);
            txtItemName=itemView.findViewById(R.id.txtItemName);
            txtItemQty=itemView.findViewById(R.id.txtItemQty);
            txtItemUnit=itemView.findViewById(R.id.txtItemUnit);
            txtItemPrice=itemView.findViewById(R.id.txtItemPrice);
            txtItemAmount=itemView.findViewById(R.id.txtItemAmount);
        }

    }

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_table_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Order order=orderList.get(position);
        holder.txtItemNo.setText(String.valueOf(position+1));
        holder.txtItemName.setText(order.getItemName());
        holder.txtItemQty.setText(order.getItemQuantity());
        holder.txtItemUnit.setText(order.getItemUnit());
        holder.txtItemPrice.setText(order.getItemPrice());
        String Amount= String.valueOf(Integer.parseInt(order.getItemQuantity()) * Integer.parseInt(order.getItemPrice()));
        holder.txtItemAmount.setText(Amount);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


}
