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
import com.zeeshan.foodjaradmin.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> implements Filterable {

    private List<User> userList;
    private List<User> userListFull;


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtUsername, txtPhone, txtAddress, txtShopName;


        MyViewHolder(View itemView) {
            super(itemView);

            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtAddress = itemView.findViewById(R.id.txtShopAddress);
            txtShopName = itemView.findViewById(R.id.txtShopName);

        }
    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
        userListFull = new ArrayList<>(userList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_user, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = userList.get(position);
        holder.txtUsername.setText(user.getUserName());
        holder.txtAddress.setText(user.getAddress());
        holder.txtPhone.setText(user.getPhoneNumber());
        holder.txtShopName.setText(user.getShopName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public Filter getFilter() {
        return productsFilter;
    }

    private Filter productsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredUsers = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredUsers.addAll(userListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (User user : userListFull) {
                    if (user.getUserName().toLowerCase().contains(filterPattern) ||
                            user.getShopName().toLowerCase().contains(filterPattern) ||
                            user.getAddress().toLowerCase().contains(filterPattern) ||
                            user.getPhoneNumber().toLowerCase().contains(filterPattern)) {
                        filteredUsers.add(user);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredUsers;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            userList.clear();
            userList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}

