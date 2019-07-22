package com.zeeshan.foodjar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeeshan.foodjar.R;
import com.zeeshan.foodjar.entities.OrderRequest;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private List<OrderRequest> orderRequestList;

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtOrderID, txtOrderStatus, txtOrderItemCount, txtOrderTotalAmount;
        String orderID;


        MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            txtOrderID = itemView.findViewById(R.id.txtOrderID);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtOrderItemCount = itemView.findViewById(R.id.txtOrderItemCount);
            txtOrderTotalAmount = itemView.findViewById(R.id.txtTotalAmountOrder);

        }

        @Override
        public void onClick(View view) {

        }
    }

    public OrderAdapter(List<OrderRequest> orderRequestList) {
        this.orderRequestList = orderRequestList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_order, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrderRequest orderRequest = orderRequestList.get(position);
        holder.txtOrderID.setText(orderRequest.getOrderID());
        holder.txtOrderStatus.setText(orderRequest.getOrderStatus());
        holder.txtOrderItemCount.setText(orderRequest.getItemCount());
        holder.txtOrderTotalAmount.setText(orderRequest.getTotalAmount());

        holder.orderID = orderRequest.getUserID();

    }

    @Override
    public int getItemCount() {
        return orderRequestList.size();
    }

}
