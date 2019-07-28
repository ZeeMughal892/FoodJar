package com.zeeshan.foodjar.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.zeeshan.foodjar.UserOrderDetails;
import com.zeeshan.foodjar.R;
import com.zeeshan.foodjar.entities.OrderRequest;

import java.util.List;

public class OrderRequestAdapter extends RecyclerView.Adapter<OrderRequestAdapter.MyViewHolder> {

    private List<OrderRequest> orderRequestList;
    ConstraintLayout cardViewOrderList;
    String orderID;

    class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView txtOrderID, txtOrderStatus, txtOrderItemCount, txtOrderTotalAmount;

        MyViewHolder(View itemView) {
            super(itemView);

            txtOrderID = itemView.findViewById(R.id.txtOrderID);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtOrderItemCount = itemView.findViewById(R.id.txtOrderItemCount);
            txtOrderTotalAmount = itemView.findViewById(R.id.txtTotalAmountOrder);
            cardViewOrderList = itemView.findViewById(R.id.cardViewOrderList);
        }

    }

    public OrderRequestAdapter(List<OrderRequest> orderRequestList) {
        this.orderRequestList = orderRequestList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_order, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(itemView);
        cardViewOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderID = orderRequestList.get(myViewHolder.getAdapterPosition()).getOrderID();
               Intent intent=new Intent(itemView.getContext(), UserOrderDetails.class);
               UserOrderDetails.OrderID=orderID;
               itemView.getContext().startActivity(intent);
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrderRequest orderRequest = orderRequestList.get(position);
        String amount=" SAR " +orderRequest.getTotalAmount();
        holder.txtOrderID.setText(orderRequest.getOrderID());
        holder.txtOrderStatus.setText(orderRequest.getOrderStatus());
        holder.txtOrderItemCount.setText(orderRequest.getItemCount());
        holder.txtOrderTotalAmount.setText(amount);


    }

    @Override
    public int getItemCount() {
        return orderRequestList.size();
    }

}
