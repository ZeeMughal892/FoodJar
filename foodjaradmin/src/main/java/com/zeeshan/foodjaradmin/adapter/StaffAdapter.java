package com.zeeshan.foodjaradmin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeeshan.foodjaradmin.R;
import com.zeeshan.foodjaradmin.entities.Staff;

import java.util.ArrayList;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.MyViewHolder> implements Filterable {

    private List<Staff> staffList;
    private List<Staff> staffListFull;


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtStaffName, txtStaffPhone;


        MyViewHolder(View itemView) {
            super(itemView);

            txtStaffName = itemView.findViewById(R.id.txtStaffName);
            txtStaffPhone = itemView.findViewById(R.id.txtStaffPhone);
        }
    }

    public StaffAdapter(List<Staff> staffList) {
        this.staffList = staffList;
        staffListFull = new ArrayList<>(staffList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_staff_member, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Staff staff = staffList.get(position);
        holder.txtStaffName.setText(staff.getStaffName());
        holder.txtStaffPhone.setText(staff.getStaffPhoneNo());
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    @Override
    public Filter getFilter() {
        return productsFilter;
    }

    private Filter productsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Staff> filteredStaff = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredStaff.addAll(staffListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Staff staff : staffListFull) {
                    if (staff.getStaffName().toLowerCase().contains(filterPattern) ||
                            staff.getStaffPhoneNo().toLowerCase().contains(filterPattern)) {
                        filteredStaff.add(staff);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredStaff;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            staffList.clear();
            staffList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}

