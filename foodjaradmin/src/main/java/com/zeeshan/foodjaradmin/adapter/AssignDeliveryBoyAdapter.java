package com.zeeshan.foodjaradmin.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zeeshan.foodjaradmin.OrderDetails;
import com.zeeshan.foodjaradmin.PendingOrders;
import com.zeeshan.foodjaradmin.R;
import com.zeeshan.foodjaradmin.entities.DeliveryBoy;
import com.zeeshan.foodjaradmin.entities.Order;
import com.zeeshan.foodjaradmin.entities.OrderRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AssignDeliveryBoyAdapter extends RecyclerView.Adapter<AssignDeliveryBoyAdapter.MyViewHolder> implements Filterable {

    private List<DeliveryBoy> deliveryBoyList;
    private List<DeliveryBoy> deliveryBoyListFull;
    CardView cardViewBoys;
    DatabaseReference databaseOrderRequests;
    OrderRequest currentOrder=new OrderRequest();

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtBoyName, txtBoyPhone;

        MyViewHolder(View itemView) {
            super(itemView);
            txtBoyName = itemView.findViewById(R.id.txtBoyName);
            txtBoyPhone = itemView.findViewById(R.id.txtBoyPhone);
            cardViewBoys = itemView.findViewById(R.id.cardBoys);
        }
    }

    public AssignDeliveryBoyAdapter(List<DeliveryBoy> deliveryBoyList) {
        this.deliveryBoyList = deliveryBoyList;
        deliveryBoyListFull = new ArrayList<>(deliveryBoyList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_delivery_boy, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(itemView);

        databaseOrderRequests = FirebaseDatabase.getInstance().getReference("orderRequests");

        cardViewBoys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String orderId = OrderDetails.OrderID;
                databaseOrderRequests.child(orderId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        currentOrder = dataSnapshot.getValue(OrderRequest.class);
                        int position = myViewHolder.getAdapterPosition();
                        String amount = currentOrder.getTotalAmount();
                        String userId = currentOrder.getUserID();
                        String status = "ASSIGNED";
                        String assignTo = deliveryBoyList.get(position).getBoyID();
                        String itemCount = currentOrder.getItemCount();
                        List<Order> orders = new ArrayList<>();
                        String boyName = deliveryBoyList.get(position).getBoyName();
                        orders = currentOrder.getOrderList();
                        OrderRequest orderRequest = new OrderRequest(orderId, userId, orders, amount, status, assignTo, itemCount);
                        databaseOrderRequests.child(orderId).setValue(orderRequest);
                        Toast.makeText(itemView.getContext(), "Order Assign To : " + boyName, Toast.LENGTH_SHORT).show();
                        itemView.getContext().startActivity(new Intent(itemView.getContext(), PendingOrders.class));

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(itemView.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DeliveryBoy deliveryBoy = deliveryBoyList.get(position);
        holder.txtBoyName.setText(deliveryBoy.getBoyName());
        holder.txtBoyPhone.setText(deliveryBoy.getBoyPhone());
    }

    @Override
    public int getItemCount() {
        return deliveryBoyList.size();
    }

    @Override
    public Filter getFilter() {
        return productsFilter;
    }

    private Filter productsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DeliveryBoy> filteredBoys = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredBoys.addAll(deliveryBoyListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (DeliveryBoy deliveryBoy : deliveryBoyListFull) {
                    if (deliveryBoy.getBoyName().toLowerCase().contains(filterPattern) ||
                            deliveryBoy.getBoyPhone().toLowerCase().contains(filterPattern)) {
                        filteredBoys.add(deliveryBoy);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredBoys;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            deliveryBoyList.clear();
            deliveryBoyList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}

