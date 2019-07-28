package com.zeeshan.foodjaradmin.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zeeshan.foodjaradmin.R;
import com.zeeshan.foodjaradmin.SearchItem;
import com.zeeshan.foodjaradmin.entities.DeliveryBoy;

import java.util.ArrayList;
import java.util.List;

public class DeliveryBoyAdapter extends RecyclerView.Adapter<DeliveryBoyAdapter.MyViewHolder> implements Filterable {

    private List<DeliveryBoy> deliveryBoyList;
    private List<DeliveryBoy> deliveryBoyListFull;




    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtBoyName, txtBoyPhone;

        MyViewHolder(View itemView) {
            super(itemView);

            txtBoyName = itemView.findViewById(R.id.txtBoyName);
            txtBoyPhone = itemView.findViewById(R.id.txtBoyPhone);

        }
    }

    public DeliveryBoyAdapter(List<DeliveryBoy> deliveryBoyList) {
        this.deliveryBoyList = deliveryBoyList;
        deliveryBoyListFull = new ArrayList<>(deliveryBoyList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_delivery_boy, viewGroup, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemView);



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

